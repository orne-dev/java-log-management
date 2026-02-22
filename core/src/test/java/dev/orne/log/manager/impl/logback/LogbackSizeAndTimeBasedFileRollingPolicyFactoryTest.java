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

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.RollingPeriod;
import dev.orne.log.manager.SizeAndTimeBasedFileRollingPolicy;
import dev.orne.log.manager.SizeBasedFileRollingPolicy;
import dev.orne.log.manager.TimeBasedFileRollingPolicy;

/**
 * Unit tests for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2026-02
 * @since 1.0
 */
@Tag("ut")
class LogbackSizeAndTimeBasedFileRollingPolicyFactoryTest {

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#LogbackSizeAndTimeBasedFileRollingPolicyFactory(LogManagementConfig)}.
     */
    @Test
    void givenDefaultConfig_whenConstructor_willUsedDefaultCompressExtension() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        assertEquals(
                LogManagementConfig.Defaults.ROLLING_COMPRESS_EXTENSION,
                factory.getCompressedSuffix());
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#LogbackSizeAndTimeBasedFileRollingPolicyFactory(LogManagementConfig)}.
     */
    @Test
    void givenCustomConfig_whenConstructor_willUsedCustomCompressExtension() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final String compressExtension = ".gz";
        given(config.get(LogManagementConfig.Properties.ROLLING_COMPRESS_EXTENSION)).willReturn(compressExtension);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        assertEquals(
                compressExtension,
                factory.getCompressedSuffix());
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#supports(Class)}.
     */
    @Test
    void givenDefaultInstance_whenSupports_willOnlySupportSizeBasedPolicies() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        assertTrue(factory.supports(SizeAndTimeBasedFileRollingPolicy.class));
        assertFalse(factory.supports(TimeBasedFileRollingPolicy.class));
        assertFalse(factory.supports(SizeBasedFileRollingPolicy.class));
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenMissingPolicyConfig_whenCreate_willThrowException() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender appender = mock(ManagedAppender.class);
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, null));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, appender));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(context, null));
        given(appender.getFilename()).willReturn("someFile");
        given(appender.getFileRollingPolicy()).willReturn(Optional.empty());
        assertThrows(
                LogManagementException.class,
                () -> factory.create(context, appender));
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenUnsupportedPolicyConfig_whenCreate_willThrowException() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender appender = mock(ManagedAppender.class);
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, null));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, appender));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(context, null));
        final FileRollingPolicy policyConfig = mock(FileRollingPolicy.class);
        given(appender.getFilename()).willReturn("someFile");
        given(appender.getFileRollingPolicy()).willReturn(Optional.of(policyConfig));
        assertThrows(
                LogManagementException.class,
                () -> factory.create(context, appender));
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenMinimalPolicyConfig_whenCreate_willCreateLogbackPolicy() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender appender = mock(ManagedAppender.class);
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, null));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, appender));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(context, null));
        final SizeAndTimeBasedFileRollingPolicy policyConfig = SizeAndTimeBasedFileRollingPolicy
                .builder()
                .withPeriod(RollingPeriod.DAILY)
                .withFileSize(10L * 1024 * 1024)
                .build();
        given(appender.getFilename()).willReturn("someFile");
        given(appender.getFileRollingPolicy()).willReturn(Optional.of(policyConfig));
        final LogbackFileRollingPolicy result = assertDoesNotThrow(() ->
                factory.create(context, appender));
        assertSame(policyConfig, result.getPolicyConfig());
        final SizeAndTimeBasedRollingPolicy<?> rollingPolicy = assertInstanceOf(
                SizeAndTimeBasedRollingPolicy.class,
                result.getRollingPolicy());
        assertSame(context, rollingPolicy.getContext());
        assertEquals(
                String.format(
                        "someFile.%s.%%i.log",
                        LogbackTimeBasedFileRollingPolicyFactory.periodToPattern(
                            policyConfig.getPeriod())),
                rollingPolicy.getFileNamePattern());
        assertEquals(policyConfig.getMaxHistory(), rollingPolicy.getMaxHistory());
        assertSame(
                rollingPolicy,
                result.getTriggeringPolicy());
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenCompressedPolicyConfig_whenCreate_willCreateLogbackPolicy() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender appender = mock(ManagedAppender.class);
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, null));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, appender));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(context, null));
        final SizeAndTimeBasedFileRollingPolicy policyConfig = SizeAndTimeBasedFileRollingPolicy
                .builder()
                .withPeriod(RollingPeriod.WEEKLY)
                .withFileSize(10L * 1024 * 1024)
                .withCompressed(true)
                .build();
        given(appender.getFilename()).willReturn("someFile");
        given(appender.getFileRollingPolicy()).willReturn(Optional.of(policyConfig));
        final LogbackFileRollingPolicy result = assertDoesNotThrow(() ->
                factory.create(context, appender));
        assertSame(policyConfig, result.getPolicyConfig());
        final SizeAndTimeBasedRollingPolicy<?> rollingPolicy = assertInstanceOf(
                SizeAndTimeBasedRollingPolicy.class,
                result.getRollingPolicy());
        assertSame(context, rollingPolicy.getContext());
        assertEquals(
                String.format(
                        "someFile.%s.%%i.log%s",
                        LogbackTimeBasedFileRollingPolicyFactory.periodToPattern(
                            policyConfig.getPeriod()),
                        factory.getCompressedSuffix()),
                rollingPolicy.getFileNamePattern());
        assertEquals(policyConfig.getMaxHistory(), rollingPolicy.getMaxHistory());
        assertSame(
                rollingPolicy,
                result.getTriggeringPolicy());
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenMaxHistoryPolicyConfig_whenCreate_willCreateLogbackPolicy() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender appender = mock(ManagedAppender.class);
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, null));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, appender));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(context, null));
        final SizeAndTimeBasedFileRollingPolicy policyConfig = SizeAndTimeBasedFileRollingPolicy
                .builder()
                .withPeriod(RollingPeriod.MONTHLY)
                .withFileSize(10L * 1024 * 1024)
                .withMaxHistory(5)
                .build();
        given(appender.getFilename()).willReturn("someFile");
        given(appender.getFileRollingPolicy()).willReturn(Optional.of(policyConfig));
        final LogbackFileRollingPolicy result = assertDoesNotThrow(() ->
                factory.create(context, appender));
        assertSame(policyConfig, result.getPolicyConfig());
        final SizeAndTimeBasedRollingPolicy<?> rollingPolicy = assertInstanceOf(
                SizeAndTimeBasedRollingPolicy.class,
                result.getRollingPolicy());
        assertSame(context, rollingPolicy.getContext());
        assertEquals(
                String.format(
                        "someFile.%s.%%i.log",
                        LogbackTimeBasedFileRollingPolicyFactory.periodToPattern(
                            policyConfig.getPeriod())),
                rollingPolicy.getFileNamePattern());
        assertEquals(policyConfig.getMaxHistory(), rollingPolicy.getMaxHistory());
        assertSame(
                rollingPolicy,
                result.getTriggeringPolicy());
    }

    /**
     * Test for {@link LogbackSizeAndTimeBasedFileRollingPolicyFactory#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenMaxSizePolicyConfig_whenCreate_willCreateLogbackPolicy() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender appender = mock(ManagedAppender.class);
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, null));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(null, appender));
        assertThrows(
                NullPointerException.class,
                () -> factory.create(context, null));
        final SizeAndTimeBasedFileRollingPolicy policyConfig = SizeAndTimeBasedFileRollingPolicy
                .builder()
                .withPeriod(RollingPeriod.MONTHLY)
                .withFileSize(10L * 1024 * 1024)
                .withMaxHistorySize(10L * 1024L * 1024L)
                .build();
        given(appender.getFilename()).willReturn("someFile");
        given(appender.getFileRollingPolicy()).willReturn(Optional.of(policyConfig));
        final LogbackFileRollingPolicy result = assertDoesNotThrow(() ->
                factory.create(context, appender));
        assertSame(policyConfig, result.getPolicyConfig());
        final SizeAndTimeBasedRollingPolicy<?> rollingPolicy = assertInstanceOf(
                SizeAndTimeBasedRollingPolicy.class,
                result.getRollingPolicy());
        assertSame(context, rollingPolicy.getContext());
        assertEquals(
                String.format(
                        "someFile.%s.%%i.log",
                        LogbackTimeBasedFileRollingPolicyFactory.periodToPattern(
                            policyConfig.getPeriod())),
                rollingPolicy.getFileNamePattern());
        assertEquals(policyConfig.getMaxHistory(), rollingPolicy.getMaxHistory());
        assertSame(
                rollingPolicy,
                result.getTriggeringPolicy());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory factory =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        assertNotEquals(factory, (Object) null);
        assertEquals(factory, factory);
        assertEquals(factory.hashCode(), factory.hashCode());
        assertNotEquals(factory, new Object());
        final String compressExtension = ".mock";
        given(config.get(LogManagementConfig.Properties.ROLLING_COMPRESS_EXTENSION)).willReturn(compressExtension);
        final LogbackSizeAndTimeBasedFileRollingPolicyFactory other =
                new LogbackSizeAndTimeBasedFileRollingPolicyFactory(config);
        assertNotEquals(factory, other);
    }
}
