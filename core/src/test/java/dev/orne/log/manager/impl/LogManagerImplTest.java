package dev.orne.log.manager.impl;

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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.log.manager.Appender;
import dev.orne.log.manager.Level;
import dev.orne.log.manager.LogManagementCoordinator;
import dev.orne.log.manager.LogManagementEngine;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.Logger;
import dev.orne.log.manager.ManagedAppenderConfig;
import dev.orne.log.manager.TestUtils;

/**
 * Unit tests for {@link LogManagerImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LogManagerImplTest {

    private static final String TEST_LOGGER_NAME = "test-logger";
    private static final String TEST_LEVEL_NAME = "test-level";
    private static final String TEST_APPENDER_NAME = "test-appender";

    private @Mock LogManagementCoordinator coordinator;
    private @Mock LogManagementEngine engine;
    private LogManagerImpl instance;
    private AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        instance = new LogManagerImpl(coordinator, engine);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        mocks.close();
    }

    /**
     * Test for protected component getters for extension.
     * 
     * @see LogManagerImpl#getCoordinator()
     * @see LogManagerImpl#getEngine()
     */
    @Test
    void testExtensionGetters() {
        assertSame(coordinator, instance.getCoordinator());
        TestUtils.assertProtectedMethod(
                LogManagerImpl.class,
                "getCoordinator");
        assertSame(engine, instance.getEngine());
        TestUtils.assertProtectedMethod(
                LogManagerImpl.class,
                "getEngine");
    }

    /**
     * Test for {@link LogManagerImpl#getLevels()}.
     */
    @Test
    void testGetLevels()
    throws LogManagementException {
        final List<Level> mockResult = List.of();
        given(engine.getLevels()).willReturn(mockResult);
        final List<Level> result = instance.getLevels();
        assertSame(mockResult, result);
        then(engine).should().getLevels();
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#getRoot()}.
     */
    @Test
    void testGetRoot()
    throws LogManagementException {
        final Logger mockResult = mock(Logger.class);
        given(engine.getRoot()).willReturn(mockResult);
        final Logger result = instance.getRoot();
        assertSame(mockResult, result);
        then(engine).should().getRoot();
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#existsLogger(String)}.
     */
    @Test
    void testExistsLogger()
    throws LogManagementException {
        final Optional<Logger> mockResult = Optional.empty();
        given(engine.findLogger(TEST_LOGGER_NAME)).willReturn(Optional.empty());
        final Optional<Logger> result = instance.existsLogger(TEST_LOGGER_NAME);
        assertSame(mockResult, result);
        then(engine).should().findLogger(TEST_LOGGER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#getLogger(String)}.
     */
    @Test
    void testGetLogger()
    throws LogManagementException {
        final Logger mockResult = mock(Logger.class);
        given(engine.getLogger(TEST_LOGGER_NAME)).willReturn(mockResult);
        final Logger result = instance.getLogger(TEST_LOGGER_NAME);
        assertSame(mockResult, result);
        then(engine).should().getLogger(TEST_LOGGER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#getChildren(String)}.
     */
    @Test
    void testGetChildren()
    throws LogManagementException {
        final Map<String, Logger> mockResult = Map.of();
        given(engine.getChildren(TEST_LOGGER_NAME)).willReturn(mockResult);
        final Map<String, Logger> result = instance.getChildren(TEST_LOGGER_NAME);
        assertSame(mockResult, result);
        then(engine).should().getChildren(TEST_LOGGER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#setLevel(String, String)}.
     */
    @Test
    void testSetLevel()
    throws LogManagementException {
        instance.setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
        then(coordinator).should().setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
        then(coordinator).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#getAppenders()}.
     */
    @Test
    void testGetAppenders()
    throws LogManagementException {
        final List<Appender> mockResult = List.of();
        given(engine.getAppenders()).willReturn(mockResult);
        final List<Appender> result = instance.getAppenders();
        assertSame(mockResult, result);
        then(engine).should().getAppenders();
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#getAppenders(String)}.
     */
    @Test
    void testGetLoggerAppenders()
    throws LogManagementException {
        final List<String> mockResult = List.of();
        given(engine.getAppenders(TEST_LOGGER_NAME)).willReturn(mockResult);
        final List<String> result = instance.getAppenders(TEST_LOGGER_NAME);
        assertSame(mockResult, result);
        then(engine).should().getAppenders(TEST_LOGGER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#getAppender(String)}.
     */
    @Test
    void testGetAppender()
    throws LogManagementException {
        final Appender mockResult = mock(Appender.class);
        given(engine.getAppender(TEST_APPENDER_NAME)).willReturn(mockResult);
        final Appender result = instance.getAppender(TEST_APPENDER_NAME);
        assertSame(mockResult, result);
        then(engine).should().getAppender(TEST_APPENDER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#createAppender(ManagedAppenderConfig).
     */
    @Test
    void testCreateAppender()
    throws LogManagementException {
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        instance.createAppender(config);
        then(coordinator).should().createAppender(config);
        then(coordinator).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#deleteAppender(String)}.
     */
    @Test
    void testDeleteAppender()
    throws LogManagementException {
        instance.deleteAppender(TEST_APPENDER_NAME);
        then(coordinator).should().deleteAppender(TEST_APPENDER_NAME);
        then(coordinator).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#attachAppender(String, String)}.
     */
    @Test
    void testAttachAppender()
    throws LogManagementException {
        instance.attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(coordinator).should().attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(coordinator).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link LogManagerImpl#detachAppender(String, String)}.
     */
    @Test
    void testDetachAppender()
    throws LogManagementException {
        instance.detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(coordinator).should().detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(coordinator).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
    }
}
