package dev.orne.log.manager;

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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LogManager}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LogManagerTest {

    private static final String TEST_LOGGER_NAME = "test-logger";
    private static final String TEST_LEVEL_NAME = "test-level";
    private static final String TEST_APPENDER_NAME = "test-appender";

    /**
     * Test for {@link LogManager#existsLogger(Logger)}
     * default implementation.
     */
    @Test
    void testExistsLogger()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final Optional<Logger> mockResult = Optional.empty();
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(instance.existsLogger(TEST_LOGGER_NAME)).willReturn(mockResult);
        final Optional<Logger> result = instance.existsLogger(logger);
        assertSame(mockResult, result);
        then(instance).should().existsLogger(TEST_LOGGER_NAME);
    }

    /**
     * Test for {@link LogManager#getLogger(Logger)}
     * default implementation.
     */
    @Test
    void testGetLogger()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final Logger mockResult = mock(Logger.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(instance.getLogger(TEST_LOGGER_NAME)).willReturn(mockResult);
        final Logger result = instance.getLogger(logger);
        assertSame(mockResult, result);
        then(instance).should().getLogger(TEST_LOGGER_NAME);
    }

    /**
     * Test for {@link LogManager#getChildren(Logger)}
     * default implementation.
     */
    @Test
    void testGetChildren()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final Map<String, Logger> mockResult = Map.of();
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(instance.getChildren(TEST_LOGGER_NAME)).willReturn(mockResult);
        final Map<String, Logger> result = instance.getChildren(logger);
        assertSame(mockResult, result);
        then(instance).should().getChildren(TEST_LOGGER_NAME);
    }

    /**
     * Test for {@link LogManager#setLevel(Logger, Level)}
     * default implementation.
     */
    @Test
    void testSetLevelLoggerLevel()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final Level level = mock(Level.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(level.getName()).willReturn(TEST_LEVEL_NAME);
        instance.setLevel(logger, level);
        then(instance).should().setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
    }

    /**
     * Test for {@link LogManager#setLevel(Logger, Level)}
     * default implementation.
     */
    @Test
    void testSetLevelLoggerNullLevel()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        instance.setLevel(logger, (Level) null);
        then(instance).should().setLevel(TEST_LOGGER_NAME, (String) null);
    }

    /**
     * Test for {@link LogManager#setLevel(Logger, String)}
     * default implementation.
     */
    @Test
    void testSetLevelLoggerString()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        instance.setLevel(logger, TEST_LEVEL_NAME);
        then(instance).should().setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
    }

    /**
     * Test for {@link LogManager#setLevel(String, Level)}
     * default implementation.
     */
    @Test
    void testSetLevelStringLevel()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Level level = mock(Level.class);
        given(level.getName()).willReturn(TEST_LEVEL_NAME);
        instance.setLevel(TEST_LOGGER_NAME, level);
        then(instance).should().setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
    }

    /**
     * Test for {@link LogManager#setLevel(String, Level)}
     * default implementation.
     */
    @Test
    void testSetLevelStringNullLevel()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        instance.setLevel(TEST_LOGGER_NAME, (Level) null);
        then(instance).should().setLevel(TEST_LOGGER_NAME, (String) null);
    }

    /**
     * Test for {@link LogManager#getAppenders(Logger)}
     * default implementation.
     */
    @Test
    void testGetAppenders()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final List<String> mockResult = List.of();
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(instance.getAppenders(TEST_LOGGER_NAME)).willReturn(mockResult);
        final List<String> result = instance.getAppenders(logger);
        assertSame(mockResult, result);
        then(instance).should().getAppenders(TEST_LOGGER_NAME);
    }

    /**
     * Test for {@link LogManager#getAppender(Appender)}
     * default implementation.
     */
    @Test
    void testGetAppender()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Appender appender = mock(Appender.class);
        final Appender mockResult = mock(Appender.class);
        given(appender.getName()).willReturn(TEST_APPENDER_NAME);
        given(instance.getAppender(TEST_APPENDER_NAME)).willReturn(mockResult);
        final Appender result = instance.getAppender(appender);
        assertSame(mockResult, result);
        then(instance).should().getAppender(TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#deleteAppender(Appender)}
     * default implementation.
     */
    @Test
    void testDeleteAppender()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Appender appender = mock(Appender.class);
        given(appender.getName()).willReturn(TEST_APPENDER_NAME);
        instance.deleteAppender(appender);
        then(instance).should().deleteAppender(TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#attachAppender(Logger, Appender)}
     * default implementation.
     */
    @Test
    void testAttachAppenderLoggerAppender()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final Appender appender = mock(Appender.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(appender.getName()).willReturn(TEST_APPENDER_NAME);
        instance.attachAppender(logger, appender);
        then(instance).should().attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#attachAppender(Logger, String)}
     * default implementation.
     */
    @Test
    void testAttachAppenderLoggerString()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        instance.attachAppender(logger, TEST_APPENDER_NAME);
        then(instance).should().attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#attachAppender(String, Appender)}
     * default implementation.
     */
    @Test
    void testAttachAppenderStringAppender()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Appender appender = mock(Appender.class);
        given(appender.getName()).willReturn(TEST_APPENDER_NAME);
        instance.attachAppender(TEST_LOGGER_NAME, appender);
        then(instance).should().attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#detachAppender(Logger, Appender)}
     * default implementation.
     */
    @Test
    void testDetachAppenderLoggerAppender()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        final Appender appender = mock(Appender.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        given(appender.getName()).willReturn(TEST_APPENDER_NAME);
        instance.detachAppender(logger, appender);
        then(instance).should().detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#detachAppender(Logger, String)}
     * default implementation.
     */
    @Test
    void testDetachAppenderLoggerString()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Logger logger = mock(Logger.class);
        given(logger.getName()).willReturn(TEST_LOGGER_NAME);
        instance.detachAppender(logger, TEST_APPENDER_NAME);
        then(instance).should().detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
    }

    /**
     * Test for {@link LogManager#detachAppender(String, Appender)}
     * default implementation.
     */
    @Test
    void testDetachAppenderStringAppender()
    throws LogManagementException {
        final LogManager instance = spy(LogManager.class);
        final Appender appender = mock(Appender.class);
        given(appender.getName()).willReturn(TEST_APPENDER_NAME);
        instance.detachAppender(TEST_LOGGER_NAME, appender);
        then(instance).should().detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
    }
}
