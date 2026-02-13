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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import dev.orne.config.Config;
import dev.orne.log.manager.Appender;
import dev.orne.log.manager.AppenderNotFoundException;
import dev.orne.log.manager.Level;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.Logger;
import dev.orne.log.manager.LoggerNotFoundException;
import dev.orne.log.manager.ManagedAppender;

/**
 * Unit tests for {@link LogbackManagementEngine} read-only operations.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LogbackManagementEngineReadOnlyTest {

    private static final String PARENT_LOGGER_NAME =
            LogbackManagementEngineReadOnlyTest.class.getName();
    private static final String EXISTING_LOGGER_KEY =
            "TestLogger";
    private static final String EXISTING_LOGGER_NAME =
            PARENT_LOGGER_NAME + "." + EXISTING_LOGGER_KEY;
    private static final String MISSING_LOGGER_NAME =
            PARENT_LOGGER_NAME + ".MissingLogger";
    private static final String CREATED_LOGGER_NAME =
            PARENT_LOGGER_NAME + ".CreatedLogger";

    /** The shared instance for read-only tests. */
    private static LogbackManagementEngine instance;

    /**
     * Creates the shared instance and pre-existing Logger.
     */
    @BeforeAll
    static void createInstance() {
        final HashMap<String, String> props = new HashMap<>();
        final LogManagementConfig config =
                ((Config) props::get).as(LogManagementConfig.class);
        instance = new LogbackManagementEngine(config);
        LoggerFactory.getLogger(PARENT_LOGGER_NAME);
        LoggerFactory.getLogger(EXISTING_LOGGER_NAME);
    }

    /**
     * Resets the Logback configuration after each test.
     * 
     * @throws LogManagementException If reset fails
     */
    @AfterEach
    void resetLogback()
    throws LogManagementException {
        instance.reset();
    }

    /**
     * Test for {@link LogbackManagementEngine#LogbackManagementEngine(LogManagementConfig)}.
     */
    @Test
    void whenDefaultConstruction_then() {
        final HashMap<String, String> props = new HashMap<>();
        final LogManagementConfig config =
                ((Config) props::get).as(LogManagementConfig.class);
        final LogbackManagementEngine result = new LogbackManagementEngine(config);
        assertSame(LoggerFactory.getILoggerFactory(), result.getContext());
        final LogbackAppenderFactory resultFactory = assertInstanceOf(
                LogbackAppenderFactory.class,
                result.getAppenderFactory());
        final LogbackFileRollingPolicies resultPolicies =
                resultFactory.getPolicies();
        assertEquals(
                LogbackFileRollingPolicies.loadSpiFactories(config),
                resultPolicies.getFactories());
        assertEquals(
                new LogbackAppenderFactory(resultPolicies, config),
                resultFactory);
    }

    /**
     * Test for {@link LogbackManagementEngine#getLevels()}.
     */
    @Test
    void whenGetLevels_thenReturnsLogbackLevels() {
        final List<Level> result = instance.getLevels();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(
                ch.qos.logback.classic.Level.TRACE.levelStr,
                result.get(0).getName());
        assertEquals(
                ch.qos.logback.classic.Level.TRACE.levelInt,
                result.get(0).getValue());
        assertEquals(
                ch.qos.logback.classic.Level.DEBUG.levelStr,
                result.get(1).getName());
        assertEquals(
                ch.qos.logback.classic.Level.DEBUG.levelInt,
                result.get(1).getValue());
        assertEquals(
                ch.qos.logback.classic.Level.INFO.levelStr,
                result.get(2).getName());
        assertEquals(
                ch.qos.logback.classic.Level.INFO.levelInt,
                result.get(2).getValue());
        assertEquals(
                ch.qos.logback.classic.Level.WARN.levelStr,
                result.get(3).getName());
        assertEquals(
                ch.qos.logback.classic.Level.WARN.levelInt,
                result.get(3).getValue());
        assertEquals(
                ch.qos.logback.classic.Level.ERROR.levelStr,
                result.get(4).getName());
        assertEquals(
                ch.qos.logback.classic.Level.ERROR.levelInt,
                result.get(4).getValue());
        assertEquals(
                ch.qos.logback.classic.Level.OFF.levelStr,
                result.get(5).getName());
        assertEquals(
                ch.qos.logback.classic.Level.OFF.levelInt,
                result.get(5).getValue());
    }

    /**
     * Test for {@link LogbackManagementEngine#getRoot()}.
     */
    @Test
    void whenGetRoot_thenReturnsRootLogger() {
        final Logger result = instance.getRoot();
        assertNotNull(result);
        assertEquals(
                org.slf4j.Logger.ROOT_LOGGER_NAME,
                result.getName());
    }

    /**
     * Test for {@link LogbackManagementEngine#findLogger(String)}.
     */
    @Test
    void givenExistingLogger_whenFindLogger_thenReturnsLogger() {
        final Optional<Logger> result = instance.findLogger(EXISTING_LOGGER_NAME);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(instance.getLogger(EXISTING_LOGGER_NAME), result.get());
    }

    /**
     * Test for {@link LogbackManagementEngine#findLogger(String)}.
     */
    @Test
    void givenMissingLogger_whenFindLogger_thenReturnsEmpty() {
        assertFalse(instance.findLogger(MISSING_LOGGER_NAME).isPresent());
    }

    /**
     * Test for {@link LogbackManagementEngine#getLogger(String)}.
     */
    @Test
    void givenExistingLogger_whenGetLogger_thenReturnsLogger() {
        final Logger result = instance.getLogger(EXISTING_LOGGER_NAME);
        assertNotNull(result);
        assertEquals(EXISTING_LOGGER_NAME, result.getName());
        assertNotNull(result.getLevel());
        assertTrue(instance.getLevels().contains(result.getLevel()));
        assertTrue(result.isLevelInherited());
    }

    /**
     * Test for {@link LogbackManagementEngine#getLogger(String)}.
     */
    @Test
    void givenMissingLogger_whenGetLogger_thenCreatesLogger() {
        final Logger result = instance.getLogger(CREATED_LOGGER_NAME);
        assertNotNull(result);
        assertEquals(CREATED_LOGGER_NAME, result.getName());
        assertNotNull(result.getLevel());
        assertTrue(instance.getLevels().contains(result.getLevel()));
        assertTrue(result.isLevelInherited());
    }

    /**
     * Test for {@link LogbackManagementEngine#getChildren(String)}.
     */
    @Test
    void givenRootLogger_whenGetChildren_thenReturnsExistingRootLoggers() {
        final String rootPackage = PARENT_LOGGER_NAME.substring(0, PARENT_LOGGER_NAME.indexOf('.'));
        final Map<String, Logger> result = instance.getChildren(org.slf4j.Logger.ROOT_LOGGER_NAME);
        assertNotNull(result);
        assertTrue(result.containsKey(rootPackage));
        assertEquals(
                instance.getLogger(rootPackage),
                result.get(rootPackage));
    }

    /**
     * Test for {@link LogbackManagementEngine#getChildren(String)}.
     */
    @Test
    void givenExistingLogger_whenGetChildren_thenReturnsExistingChildLoggers() {
        final Map<String, Logger> result = instance.getChildren(PARENT_LOGGER_NAME);
        assertNotNull(result);
        assertTrue(result.containsKey(EXISTING_LOGGER_KEY));
        assertEquals(
                instance.getLogger(EXISTING_LOGGER_NAME),
                result.get(EXISTING_LOGGER_KEY));
    }

    /**
     * Test for {@link LogbackManagementEngine#getChildren(String)}.
     */
    @Test
    void givenMissingLogger_whenGetChildren_thenReturnsEmpty() {
        final Map<String, Logger> result = instance.getChildren(MISSING_LOGGER_NAME);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test for {@link LogbackManagementEngine#getAppenders()}.
     */
    @Test
    void whenGetAppenders_thenReturnsConfiguredAppenders() {
        final List<Appender> result = instance.getAppenders();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .filter(appender -> appender.getName().equals("STDOUT"))
                .findAny()
                .isPresent());
        assertTrue(result.stream()
                .filter(appender -> appender.getName().equals("NOP"))
                .findAny()
                .isPresent());
    }

    /**
     * Test for {@link LogbackManagementEngine#getAppender(String)}.
     */
    @Test
    void givenExistingAppender_whenGetAppender_thenReturnsConfiguredAppender() {
        final Appender result = instance.getAppender("STDOUT");
        assertNotNull(result);
        assertEquals("STDOUT", result.getName());
        assertFalse(result instanceof ManagedAppender);
    }

    /**
     * Test for {@link LogbackManagementEngine#getAppender(String)}.
     */
    @Test
    void givenMissingAppender_whenGetAppender_throwsException() {
        assertThrows(
                AppenderNotFoundException.class,
                () -> instance.getAppender("MissingAppender"));
    }

    /**
     * Test for {@link LogbackManagementEngine#getAppenders(String)}.
     */
    @Test
    void givenExistingLogger_whenGetLoggerAppenders_thenReturnsAttachedAppenders() {
        final List<String> result = instance.getAppenders(
                LogbackManagementEngineReadOnlyTest.class.getPackageName());
        assertNotNull(result);
        assertTrue(result.contains("NOP"));
    }

    /**
     * Test for {@link LogbackManagementEngine#getAppenders(String)}.
     */
    @Test
    void givenMissingLogger_whenGetLoggerAppenders_throwsException() {
        assertThrows(
                LoggerNotFoundException.class,
                () -> instance.getAppenders(MISSING_LOGGER_NAME));
    }
}
