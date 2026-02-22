package dev.orne.log.manager.impl.logback;

/*-
 * #%L
 * Orne Log Management
 * %%
 * Copyright (C) 2022 - 2026 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import dev.orne.config.Config;
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.InvalidAppenderConfigException;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.ManagedAppenderConfig;
import dev.orne.log.manager.TestUtils;

/**
 * Unit tests for {@link LogbackAppenderFactory}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2026-02
 * @since 1.0
 */
@Tag("ut")
class LogbackAppenderFactoryTest {

    private Map<String, String> configValues;
    private LogManagementConfig config;
    private @Mock LoggerContext context;
    private @Mock LogbackFileRollingPolicies rollingPolicies;
    private AutoCloseable mocks;

    /**
     * Initialize the mocks.
     */
    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        configValues = new HashMap<>();
        config = Config.as(configValues::get, LogManagementConfig.class);
    }

    /**
     * Close the mocks.
     */
    @AfterEach
    void cleanUp() throws Exception {
        mocks.close();
    }

    /**
     * Test for protected extension points.
     * 
     * @see LogbackAppenderFactory#prepareConfig(ManagedAppenderConfig)
     * @see LogbackAppenderFactory#getOutputFile(String)
     * @see LogbackAppenderFactory#createEncoder(LoggerContext, ManagedAppender)
     * @see LogbackAppenderFactory#createLayout(LoggerContext, ManagedAppender)
     * @see LogbackAppenderFactory#createFilters(LoggerContext, ManagedAppender)
     */
    @Test
    void testExtensionPoints() {
        TestUtils.assertProtectedMethod(
                LogbackAppenderFactory.class,
                "prepareConfig",
                ManagedAppenderConfig.class);
        TestUtils.assertProtectedMethod(
                LogbackAppenderFactory.class,
                "getOutputFile",
                String.class);
        TestUtils.assertProtectedMethod(
                LogbackAppenderFactory.class,
                "createEncoder",
                LoggerContext.class,
                ManagedAppender.class);
        TestUtils.assertProtectedMethod(
                LogbackAppenderFactory.class,
                "createLayout",
                LoggerContext.class,
                ManagedAppender.class);
        TestUtils.assertProtectedMethod(
                LogbackAppenderFactory.class,
                "createFilters",
                LoggerContext.class,
                ManagedAppender.class);
    }

    /**
     * Test for {@link LogbackAppenderFactory#LogbackAppenderFactory(LogbackFileRollingPolicies, LogManagementConfig)}.
     */
    @Test
    void givenNullConfig_whenConstructor_willThrowException() {
        assertThrows(
                NullPointerException.class,
                () -> new LogbackAppenderFactory(rollingPolicies, null));
    }

    /**
     * Test for {@link LogbackAppenderFactory#LogbackAppenderFactory(LogbackFileRollingPolicies, LogManagementConfig)}.
     */
    @Test
    void givenNullPolicies_whenConstructor_willThrowException() {
        final LogManagementConfig logConfig = Config.as(config, LogManagementConfig.class);
        assertThrows(
                NullPointerException.class,
                () -> new LogbackAppenderFactory(null, logConfig));
    }

    /**
     * Test for {@link LogbackAppenderFactory#LogbackAppenderFactory(LogbackFileRollingPolicies, LogManagementConfig)}.
     */
    @Test
    void givenEmptyConfig_whenConstructor_willUseDefaults() {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        assertEquals(
                Path.of(LogManagementConfig.Defaults.APPENDER_BASE_DIR),
                instance.getBaseDir());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FILE_PATTERN,
                instance.getFilePattern().pattern());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FILE_EXTENSION,
                instance.getFileExtension());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FALLBACK_CHARSET,
                instance.getFallbackCharset().name());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FALLBACK_FORMAT,
                instance.getFallbackFormat());
    }

    /**
     * Test for {@link LogbackAppenderFactory#LogbackAppenderFactory(LogbackFileRollingPolicies, LogManagementConfig)}.
     */
    @Test
    void givenCustomConfig_whenConstructor_willUseProvidedConfig() {
        configValues.put(
                LogManagementConfig.Properties.APPENDER_BASE_DIR,
                "/var/log/myapp/");
        configValues.put(
                LogManagementConfig.Properties.APPENDER_FILE_PATTERN,
                "custom-[\\w\\d]*");
        configValues.put(
                LogManagementConfig.Properties.APPENDER_FILE_EXTENSION,
                ".managed.log");
        configValues.put(
                LogManagementConfig.Properties.APPENDER_FALLBACK_CHARSET,
                StandardCharsets.UTF_16.name());
        configValues.put(
                LogManagementConfig.Properties.APPENDER_FALLBACK_FORMAT,
                "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        assertEquals(
                Path.of("/var/log/myapp/"),
                instance.getBaseDir());
        assertEquals(
                "custom-[\\w\\d]*",
                instance.getFilePattern().pattern());
        assertEquals(
                ".managed.log",
                instance.getFileExtension());
        assertEquals(
                StandardCharsets.UTF_16,
                instance.getFallbackCharset());
        assertEquals(
                "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n",
                instance.getFallbackFormat());
    }

    /**
     * Test that changing any configuration property results in a non-equal instance.
     */
    @ParameterizedTest
    @MethodSource("altConfigProperties")
    void testConfigChangeNotEquals(
            final String property,
            final String value) {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        final HashMap<String, String> altConfig = new HashMap<>();
        altConfig.put(
                property,
                value);
        final LogbackAppenderFactory otherInstance = new LogbackAppenderFactory(
                rollingPolicies,
                Config.as(altConfig::get, LogManagementConfig.class));
        assertNotEquals(instance, otherInstance);
        assertNotEquals(instance.hashCode(), otherInstance.hashCode());
    }

    static Stream<Arguments> altConfigProperties() {
        return Stream.of(
                Arguments.of(
                        LogManagementConfig.Properties.APPENDER_BASE_DIR,
                        "/var/log/myapp/"),
                Arguments.of(
                        LogManagementConfig.Properties.APPENDER_FILE_PATTERN,
                        "custom-[\\w\\d]*"),
                Arguments.of(
                        LogManagementConfig.Properties.APPENDER_FILE_EXTENSION,
                        ".managed.log"),
                Arguments.of(
                        LogManagementConfig.Properties.APPENDER_FALLBACK_CHARSET,
                        StandardCharsets.UTF_16.name()),
                Arguments.of(
                        LogManagementConfig.Properties.APPENDER_FALLBACK_FORMAT,
                        "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"));
    }

    /**
     * Test that changing any configuration property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        assertEquals(instance, instance);
        assertEquals(instance.hashCode(), instance.hashCode());
        assertNotEquals(instance, (Object) null);
        assertNotEquals(instance, new Object());
        LogbackAppenderFactory otherInstance = new LogbackAppenderFactory(
                mock(LogbackFileRollingPolicies.class),
                config);
        assertNotEquals(instance, otherInstance);
    }

    /**
     * Test for {@link LogbackAppenderFactory#prepareConfig(ManagedAppenderConfig)}.
     */
    @Test
    void givenEmptyConfig_whenPrepareConfig_willUseFallbackConfig() {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        final ManagedAppenderConfig appenderConfig = new ManagedAppenderConfig();
        final ManagedAppender result = instance.prepareConfig(appenderConfig);
        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getFilename());
        assertEquals(instance.getFallbackCharset(), result.getCharset());
        assertEquals(instance.getFallbackFormat(), result.getFormat());
        assertTrue(result.getFileRollingPolicy().isEmpty());
    }

    /**
     * Test for {@link LogbackAppenderFactory#prepareConfig(ManagedAppenderConfig)}.
     */
    @Test
    void givenCustomConfig_whenPrepareConfig_willUseCustomConfig() {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        final FileRollingPolicy policy = mock(FileRollingPolicy.class);
        final ManagedAppenderConfig appenderConfig = new ManagedAppenderConfig()
                .withName("myLog")
                .withFilename("myCustomLog")
                .withCharset(StandardCharsets.UTF_16)
                .withFormat("%d{HH:mm:ss} %-5level - %msg%n")
                .withFileRollingPolicy(policy);
        final ManagedAppender result = instance.prepareConfig(appenderConfig);
        assertNotNull(result);
        assertEquals("myLog", result.getName());
        assertNotNull("myCustomLog", result.getFilename());
        assertEquals(StandardCharsets.UTF_16, result.getCharset());
        assertEquals("%d{HH:mm:ss} %-5level - %msg%n", result.getFormat());
        assertTrue(result.getFileRollingPolicy().isPresent());
        assertSame(policy, result.getFileRollingPolicy().get());
    }

    /**
     * Test for {@link LogbackAppenderFactory#prepareConfig(ManagedAppenderConfig)}.
     */
    @Test
    void givenConfigWithoutFilename_whenPrepareConfig_willUseNameAsFilename() {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        final ManagedAppenderConfig appenderConfig = new ManagedAppenderConfig()
                .withName("myLog");
        final ManagedAppender result = instance.prepareConfig(appenderConfig);
        assertNotNull(result);
        assertEquals("myLog", result.getName());
        assertNotNull("myLog", result.getFilename());
        assertEquals(instance.getFallbackCharset(), result.getCharset());
        assertEquals(instance.getFallbackFormat(), result.getFormat());
        assertTrue(result.getFileRollingPolicy().isEmpty());
    }

    /**
     * Test for {@link LogbackAppenderFactory#getOutputFile(String)}.
     */
    @Test
    void givenValidFilename_whenGetOutputFile_willAddExtensionAndResolveAgainstBaseDir() {
        configValues.put(
                LogManagementConfig.Properties.APPENDER_BASE_DIR,
                "/var/log/myapp/");
        configValues.put(
                LogManagementConfig.Properties.APPENDER_FILE_EXTENSION,
                ".managed.log");
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        final String result = instance.getOutputFile("myFileName");
        assertNotNull(result);
        assertEquals(
                Path.of("/var/log/myapp/")
                    .resolve("myFileName.managed.log")
                    .toAbsolutePath(),
                Path.of(result));
    }

    /**
     * Test for {@link LogbackAppenderFactory#getOutputFile(String)}.
     */
    @Test
    void givenInvalidValidFilename_whenGetOutputFile_willThrowException() {
        configValues.put(
                LogManagementConfig.Properties.APPENDER_BASE_DIR,
                "/var/log/myapp/");
        configValues.put(
                LogManagementConfig.Properties.APPENDER_FILE_EXTENSION,
                ".managed.log");
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        assertThrows(
                InvalidAppenderConfigException.class,
                () -> instance.getOutputFile("my$Invalid/FileName"));
    }

    /**
     * Test for {@link LogbackAppenderFactory#createLayout(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenManagedAppender_whenCreateLayout_willPatternLayoutWithGivenFormat() {
        final LogbackAppenderFactory instance = new LogbackAppenderFactory(rollingPolicies, config);
        final ManagedAppender appender = mock(ManagedAppender.class);
        given(appender.getFormat()).willReturn("SomeFormat");
        final PatternLayout result = assertInstanceOf(
                PatternLayout.class,
                instance.createLayout(context, appender));
        assertNotNull(result);
        assertSame(context, result.getContext());
        assertEquals("SomeFormat", result.getPattern());
        assertFalse(result.isStarted());
    }

    /**
     * Test for {@link LogbackAppenderFactory#createEncoder(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenManagedAppender_whenCreateEncoder_willCreateLayoutWrappingEncoder() {
        final LogbackAppenderFactory instance = spy(new LogbackAppenderFactory(rollingPolicies, config));
        final ManagedAppender appender = mock(ManagedAppender.class);
        given(appender.getCharset()).willReturn(StandardCharsets.ISO_8859_1);
        final PatternLayout layout = mock(PatternLayout.class);
        given(instance.createLayout(context, appender)).willReturn(layout);
        final LayoutWrappingEncoder<?> result = assertInstanceOf(
                LayoutWrappingEncoder.class,
                instance.createEncoder(context, appender));
        assertNotNull(result);
        assertSame(context, result.getContext());
        assertEquals(StandardCharsets.ISO_8859_1, result.getCharset());
        assertSame(layout, result.getLayout());
        assertFalse(result.isStarted());
    }

    /**
     * Test for {@link LogbackAppenderFactory#create(LoggerContext, ManagedAppenderConfig)}.
     */
    @Test
    void givenAppenderConfigWithoutRollingPolicy_whenCreate_willLogbackManagedFileAppender()
    throws LogManagementException {
        final LogbackAppenderFactory instance = spy(new LogbackAppenderFactory(rollingPolicies, config));
        final ManagedAppenderConfig appenderConfig = mock(ManagedAppenderConfig.class);
        final ManagedAppender appenderData = mock(ManagedAppender.class);
        given(instance.prepareConfig(appenderConfig)).willReturn(appenderData);
        given(appenderData.getFilename()).willReturn("MockFileName");
        given(instance.getOutputFile("MockFileName")).willReturn("/var/log/myapp/MockFileName.managed.log");
        @SuppressWarnings("unchecked")
        final Encoder<ILoggingEvent> encoder = mock(Encoder.class);
        given(instance.createEncoder(context, appenderData)).willReturn(encoder);
        final LogbackFileRollingPolicy rollingPolicy = null;
        given(rollingPolicies.create(context, appenderData)).willReturn(rollingPolicy);
        final LogbackManagedAppender result = instance.create(context, appenderConfig);
        assertSame(appenderData, result.getData());
        final LogbackManagedFileAppender<?> appender = assertInstanceOf(
                LogbackManagedFileAppender.class,
                result.getAppender());
        assertEquals("/var/log/myapp/MockFileName.managed.log", appender.getFile());
        assertSame(encoder, appender.getEncoder());
        assertSame(context, appender.getContext());
        assertFalse(appender.isStarted());
    }

    /**
     * Test for {@link LogbackAppenderFactory#create(LoggerContext, ManagedAppenderConfig)}.
     */
    @Test
    void givenAppenderConfigWithRollingPolicy_whenCreate_willLogbackManagedFileAppender()
    throws LogManagementException {
        final LogbackAppenderFactory instance = spy(new LogbackAppenderFactory(rollingPolicies, config));
        final ManagedAppenderConfig appenderConfig = mock(ManagedAppenderConfig.class);
        final ManagedAppender appenderData = mock(ManagedAppender.class);
        given(instance.prepareConfig(appenderConfig)).willReturn(appenderData);
        given(appenderData.getFilename()).willReturn("MockFileName");
        given(instance.getOutputFile("MockFileName")).willReturn("/var/log/myapp/MockFileName.managed.log");
        @SuppressWarnings("unchecked")
        final Encoder<ILoggingEvent> encoder = mock(Encoder.class);
        given(instance.createEncoder(context, appenderData)).willReturn(encoder);
        final LogbackFileRollingPolicy rollingPolicy = mock(LogbackFileRollingPolicy.class);
        final RollingPolicy logbackRollingPolicy = mock(RollingPolicy.class);
        given(rollingPolicy.getRollingPolicy()).willReturn(logbackRollingPolicy);
        @SuppressWarnings("unchecked")
        final TriggeringPolicy<ILoggingEvent> logbackTriggeringConfig = mock(TriggeringPolicy.class);
        given(rollingPolicy.getTriggeringPolicy()).willReturn(logbackTriggeringConfig);
        given(rollingPolicies.create(context, appenderData)).willReturn(rollingPolicy);
        final LogbackManagedAppender result = instance.create(context, appenderConfig);
        assertSame(appenderData, result.getData());
        final LogbackManagedRollingFileAppender<?> appender = assertInstanceOf(
                LogbackManagedRollingFileAppender.class,
                result.getAppender());
        assertSame(encoder, appender.getEncoder());
        assertSame(context, appender.getContext());
        assertSame(logbackRollingPolicy, appender.getRollingPolicy());
        assertSame(logbackTriggeringConfig, appender.getTriggeringPolicy());
        assertFalse(appender.isStarted());
    }

    /**
     * Test for {@link LogbackAppenderFactory#create(LoggerContext, ManagedAppenderConfig)}.
     */
    @Test
    void givenAppenderConfigWithTimeBasedRollingPolicy_whenCreate_willLogbackManagedFileAppender()
    throws LogManagementException {
        final LogbackAppenderFactory instance = spy(new LogbackAppenderFactory(rollingPolicies, config));
        final ManagedAppenderConfig appenderConfig = mock(ManagedAppenderConfig.class);
        final ManagedAppender appenderData = mock(ManagedAppender.class);
        given(instance.prepareConfig(appenderConfig)).willReturn(appenderData);
        given(appenderData.getFilename()).willReturn("MockFileName");
        given(instance.getOutputFile("MockFileName")).willReturn("/var/log/myapp/MockFileName.managed.log");
        @SuppressWarnings("unchecked")
        final Encoder<ILoggingEvent> encoder = mock(Encoder.class);
        given(instance.createEncoder(context, appenderData)).willReturn(encoder);
        final LogbackFileRollingPolicy rollingPolicy = mock(LogbackFileRollingPolicy.class);
        @SuppressWarnings("unchecked")
        final TimeBasedRollingPolicy<ILoggingEvent> logbackRollingPolicy = mock(TimeBasedRollingPolicy.class);
        given(rollingPolicy.getRollingPolicy()).willReturn(logbackRollingPolicy);
        given(rollingPolicy.getTriggeringPolicy()).willReturn(logbackRollingPolicy);
        given(rollingPolicies.create(context, appenderData)).willReturn(rollingPolicy);
        final LogbackManagedAppender result = instance.create(context, appenderConfig);
        assertSame(appenderData, result.getData());
        final LogbackManagedRollingFileAppender<?> appender = assertInstanceOf(
                LogbackManagedRollingFileAppender.class,
                result.getAppender());
        assertSame(encoder, appender.getEncoder());
        assertSame(context, appender.getContext());
        assertSame(logbackRollingPolicy, appender.getRollingPolicy());
        assertSame(logbackRollingPolicy, appender.getTriggeringPolicy());
        assertFalse(appender.isStarted());
    }
}
