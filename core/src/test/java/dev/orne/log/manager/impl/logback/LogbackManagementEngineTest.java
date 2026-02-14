package dev.orne.log.manager.impl.logback;

/*-
 * #%L
 * Orne Log Management
 * %%
 * Copyright (C) 2022 - 2025 Orne Developments
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
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import dev.orne.config.Config;
import dev.orne.log.manager.AppenderNotFoundException;
import dev.orne.log.manager.IllegalLevelException;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.Logger;
import dev.orne.log.manager.LoggerNotFoundException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.ManagedAppenderConfig;
import dev.orne.log.manager.TestUtils;
import dev.orne.log.manager.UnmanagedAppenderException;

/**
 * Unit tests for {@link LogbackManagementEngine}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LogbackManagementEngineTest {

    /** The Logback logger context. */
    private static final LoggerContext CONTEXT =
            (LoggerContext) LoggerFactory.getILoggerFactory();
    /** Root logger name. */
    private static final String ROOT_LOGGER_NAME =
            org.slf4j.Logger.ROOT_LOGGER_NAME;
    /** Root logger. */
    private static final ch.qos.logback.classic.Logger ROOT_LOGGER =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
    /** Test logger name. */
    private static final String TEST_LOGGER_NAME =
            LogbackManagementEngineReadOnlyTest.class.getName() + ".existing";
    /** Test logger. */
    private static final ch.qos.logback.classic.Logger TEST_LOGGER =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(TEST_LOGGER_NAME);
    /** Missing logger name. */
    private static final String MISSING_LOGGER_NAME =
            LogbackManagementEngineReadOnlyTest.class.getName() + ".missing";
    /** Missing logger name. */
    private static final String TEST_APPENDER_NAME =
            "TMP_APPENDER";
    /** Test managed appender. */
    private static final ManagedAppender TEST_MANAGED_APPENDER =
            ManagedAppender.builder()
                .withName(TEST_APPENDER_NAME)
                .withCharset(StandardCharsets.UTF_8)
                .withFilename("InMemory")
                .withFormat("Mock format")
                .build();

    private static LogbackManagementEngine realEngine;
    private static LogbackStatusSnapshot initialStatus;

    private @Mock LogbackAppenderFactory appenderFactory;
    private LogbackManagementEngine instance;
    private AutoCloseable mocks;

    /**
     * Configure effective engine and capture initial status.
     */
    @BeforeAll
    static void globalSetup() {
        final HashMap<String, String> rawConfig = new HashMap<>();
        final LogManagementConfig logCfg =
                ((Config) rawConfig::get).as(LogManagementConfig.class);
        realEngine = new LogbackManagementEngine(logCfg);
        initialStatus = LogbackStatusSnapshot.capture(realEngine.getContext());
    }

    /**
     * Initialize the mocks and mock instance.
     */
    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        instance = new LogbackManagementEngine(
                CONTEXT,
                appenderFactory);
    }

    /**
     * Reset the Logback configuration and close the mocks.
     */
    @AfterEach
    void cleanUp() throws Exception {
        realEngine.reset();
        TEST_LOGGER.setLevel(null);
        mocks.close();
    }

    /**
     * Assert that the Logback status is the same as the initial one,
     * to ensure that no test has left it in a different state.
     */
    @AfterAll
    static void globalCleanUp() {
        final LogbackStatusSnapshot finalStatus = LogbackStatusSnapshot.capture(realEngine.getContext());
        initialStatus.assertSameStatus(finalStatus);
    }

    /**
     * Test for protected component getters for extension.
     * 
     * @see LogbackManagementEngine#getContext()
     * @see LogbackManagementEngine#getAppenderFactory()
     * @see LogbackManagementEngine#getAppendersRegistry()
     */
    @Test
    void testExtensionGetters() {
        assertSame(CONTEXT, instance.getContext());
        TestUtils.assertProtectedMethod(
                LogbackManagementEngine.class,
                "getContext");
        assertSame(appenderFactory, instance.getAppenderFactory());
        TestUtils.assertProtectedMethod(
                LogbackManagementEngine.class,
                "getAppenderFactory");
        TestUtils.assertProtectedMethod(
                LogbackManagementEngine.class,
                "getAppendersRegistry");
    }

    /**
     * Test for {@link LogbackManagementEngine#setLevel(String, String)}.
     */
    @Test
    void givenRootLogger_whenSetLevel_setsLoggerLevel() {
        final Logger result = instance.setLevel(ROOT_LOGGER_NAME, "ERROR");
        assertEquals(ROOT_LOGGER_NAME, result.getName());
        assertEquals("ERROR", result.getLevel().getName());
        assertFalse(result.isLevelInherited());
        assertEquals("ERROR", ROOT_LOGGER.getLevel().levelStr);
    }

    /**
     * Test for {@link LogbackManagementEngine#setLevel(String, String)}.
     */
    @Test
    void givenExistingLogger_whenSetLevel_setsLoggerLevel() {
        final Logger result = instance.setLevel(TEST_LOGGER_NAME, "ERROR");
        assertEquals(TEST_LOGGER_NAME, result.getName());
        assertEquals("ERROR", result.getLevel().getName());
        assertFalse(result.isLevelInherited());
        assertEquals("ERROR", TEST_LOGGER.getLevel().levelStr);
    }

    /**
     * Test for {@link LogbackManagementEngine#setLevel(String, String)}.
     */
    @Test
    void givenMissingLogger_whenSetLevel_throwsException() {
        assertThrows(
                LoggerNotFoundException.class,
                () -> instance.setLevel(MISSING_LOGGER_NAME, "ERROR"));
    }

    /**
     * Test for {@link LogbackManagementEngine#setLevel(String, String)}.
     */
    @Test
    void givenInvalidLevel_whenSetLevel_throwsException() {
        assertThrows(
                IllegalLevelException.class,
                () -> instance.setLevel(TEST_LOGGER_NAME, "NotALevel"));
    }

    /**
     * Test for {@link LogbackManagementEngine#setLevel(String, String)}.
     */
    @Test
    void givenRootLogger_whenSetLevelToNull_throwsException() {
        assertThrows(
                IllegalLevelException.class,
                () -> instance.setLevel(ROOT_LOGGER_NAME, null));
    }

    /**
     * Test for {@link LogbackManagementEngine#setLevel(String, String)}.
     */
    @Test
    void givenExistingLogger_whenSetLevelToNull_inheritsParentLoggerLevel() {
        TEST_LOGGER.setLevel(Level.INFO);
        final Logger result = instance.setLevel(TEST_LOGGER_NAME, null);
        assertEquals(TEST_LOGGER_NAME, result.getName());
        assertNotNull("ERROR", result.getLevel().getName());
        assertTrue(result.isLevelInherited());
        assertNull(TEST_LOGGER.getLevel());
    }

    /**
     * Test for {@link LogbackManagementEngine#createAppender(ManagedAppenderConfig)}.
     */
    @Test
    void givenAppenderConfig_whenCreateAppender_createsAppender()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        final ManagedAppender result = instance.createAppender(config);
        assertSame(TEST_MANAGED_APPENDER, result);
        assertTrue(logbackAppender.isStarted());
        assertSame(TEST_MANAGED_APPENDER, instance.getAppender(TEST_APPENDER_NAME));
        assertSame(testAppender, instance.getManagedAppender(TEST_APPENDER_NAME));
    }

    /**
     * Test for {@link LogbackManagementEngine#attachAppender(String, String)}.
     */
    @Test
    void givenExistingManagedAppender_whenAttachAppender_attachesAppenderToLogger()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        instance.createAppender(config);
        instance.attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        assertTrue(instance.getAppenders(TEST_LOGGER_NAME).contains(TEST_APPENDER_NAME));
        instance.setLevel(TEST_LOGGER_NAME, "DEBUG");
        TEST_LOGGER.debug("Test message");
        assertTrue(logbackAppender.getOutput().contains("Test message"));
    }

    /**
     * Test for {@link LogbackManagementEngine#attachAppender(String, String)}.
     */
    @Test
    void givenMissingLogger_whenAttachAppender_throwsException()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        instance.createAppender(config);
        assertThrows(
                LoggerNotFoundException.class,
                () -> instance.attachAppender(MISSING_LOGGER_NAME, TEST_APPENDER_NAME));
    }

    /**
     * Test for {@link LogbackManagementEngine#attachAppender(String, String)}.
     */
    @Test
    void givenMissingAppender_whenAttachAppender_throwsException() {
        assertThrows(
                AppenderNotFoundException.class,
                () -> instance.attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME));
    }

    /**
     * Test for {@link LogbackManagementEngine#attachAppender(String, String)}.
     */
    @Test
    void givenUnmanagedAppender_whenAttachAppender_throwsException() {
        assertThrows(
                UnmanagedAppenderException.class,
                () -> instance.attachAppender(TEST_LOGGER_NAME, "NOP"));
    }

    /**
     * Test for {@link LogbackManagementEngine#detachAppender(String, String)}.
     */
    @Test
    void givenAttachedManagedAppender_whenDetachAppender_detachsAppenderToLogger()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        instance.createAppender(config);
        instance.attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        assertTrue(instance.getAppenders(TEST_LOGGER_NAME).contains(TEST_APPENDER_NAME));
        final boolean result = instance.detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        assertTrue(result);
        assertFalse(instance.getAppenders(TEST_LOGGER_NAME).contains(TEST_APPENDER_NAME));
        instance.setLevel(TEST_LOGGER_NAME, "DEBUG");
        TEST_LOGGER.debug("Test message");
        assertFalse(logbackAppender.getOutput().contains("Test message"));
    }

    /**
     * Test for {@link LogbackManagementEngine#detachAppender(String, String)}.
     */
    @Test
    void givenNotAttachedManagedAppender_whenDetachAppender_doesNotThrow()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        instance.createAppender(config);
        final boolean result = instance.detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        assertFalse(result);
        assertFalse(instance.getAppenders(TEST_LOGGER_NAME).contains(TEST_APPENDER_NAME));
        instance.setLevel(TEST_LOGGER_NAME, "DEBUG");
        TEST_LOGGER.debug("Test message");
        assertFalse(logbackAppender.getOutput().contains("Test message"));
    }

    /**
     * Test for {@link LogbackManagementEngine#detachAppender(String, String)}.
     */
    @Test
    void givenMissingLogger_whenDetachAppender_throwsException()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        instance.createAppender(config);
        assertThrows(
                LoggerNotFoundException.class,
                () -> instance.detachAppender(MISSING_LOGGER_NAME, TEST_APPENDER_NAME));
    }

    /**
     * Test for {@link LogbackManagementEngine#detachAppender(String, String)}.
     */
    @Test
    void givenMissingManagedAppender_whenDetachAppender_throwsException() {
        assertThrows(
                AppenderNotFoundException.class,
                () -> instance.detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME));
    }

    /**
     * Test for {@link LogbackManagementEngine#detachAppender(String, String)}.
     */
    @Test
    void givenUnmanagedAppender_whenDetachAppender_throwsException() {
        assertThrows(
                UnmanagedAppenderException.class,
                () -> instance.detachAppender(TEST_LOGGER_NAME, "NOP"));
    }

    /**
     * Test for {@link LogbackManagementEngine#deleteAppender(String)}.
     */
    @Test
    void givenExistingManagedAppender_whenDeleteAppender_detachsAndDeletesAppender()
    throws LogManagementException {
        final InMemoryLogbackAppender<ILoggingEvent> logbackAppender =
                new InMemoryLogbackAppender<>(TEST_APPENDER_NAME);
        logbackAppender.setContext(CONTEXT);
        final LogbackManagedAppender testAppender =
                new LogbackManagedAppender(
                    TEST_MANAGED_APPENDER,
                    logbackAppender);
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        given(appenderFactory.create(CONTEXT, config)).willReturn(testAppender);
        instance.createAppender(config);
        instance.attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        assertTrue(instance.getAppenders(TEST_LOGGER_NAME).contains(TEST_APPENDER_NAME));
        instance.deleteAppender(TEST_APPENDER_NAME);
        assertFalse(instance.getAppenders(TEST_LOGGER_NAME).contains(TEST_APPENDER_NAME));
        assertFalse(logbackAppender.isStarted());
        instance.setLevel(TEST_LOGGER_NAME, "DEBUG");
        TEST_LOGGER.debug("Test message");
        assertFalse(logbackAppender.getOutput().contains("Test message"));
    }

    /**
     * Test for {@link LogbackManagementEngine#deleteAppender(String)}.
     */
    @Test
    void givenMissingManagedAppender_whenDeleteAppender_throwsException() {
        assertThrows(
                AppenderNotFoundException.class,
                () -> instance.deleteAppender(TEST_APPENDER_NAME));
    }

    /**
     * Test for {@link LogbackManagementEngine#deleteAppender(String)}.
     */
    @Test
    void givenUnmanagedAppender_whenDeleteAppender_throwsException() {
        assertThrows(
                UnmanagedAppenderException.class,
                () -> instance.deleteAppender("NOP"));
    }

    /**
     * Test for {@link LogbackManagementEngine#reset()}.
     */
    @Test
    void testReset()
    throws LogManagementException {
        final LogbackStatusSnapshot status = LogbackStatusSnapshot.capture(realEngine.getContext());
        instance.reset();
        final LogbackStatusSnapshot newStatus = LogbackStatusSnapshot.capture(realEngine.getContext());
        status.assertSameStatus(newStatus);
    }
}
