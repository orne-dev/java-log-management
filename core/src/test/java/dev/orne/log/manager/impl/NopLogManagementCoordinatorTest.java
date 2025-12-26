package dev.orne.log.manager.impl;

import static org.junit.jupiter.api.Assertions.assertSame;

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

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.log.manager.LogManagementEngine;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppenderConfig;
import dev.orne.log.manager.TestUtils;

/**
 * Unit tests for {@link NopLogManagementCoordinator}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class NopLogManagementCoordinatorTest {

    private static final String TEST_LOGGER_NAME = "test-logger";
    private static final String TEST_LEVEL_NAME = "test-level";
    private static final String TEST_APPENDER_NAME = "test-appender";

    private @Mock LogManagementEngine engine;
    private NopLogManagementCoordinator instance;
    private AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        instance = new NopLogManagementCoordinator(engine);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        mocks.close();
    }

    /**
     * Test for protected component getters for extension.
     * 
     * @see NopLogManagementCoordinator#getEngine()
     */
    @Test
    void testExtensionGetters() {
        assertSame(engine, instance.getEngine());
        TestUtils.assertProtectedMethod(
                NopLogManagementCoordinator.class,
                "getEngine");
    }

    /**
     * Test for {@link NopLogManagementCoordinator#setLevel(String, String)}.
     */
    @Test
    void testSetLevel()
    throws LogManagementException {
        instance.setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
        then(engine).should().setLevel(TEST_LOGGER_NAME, TEST_LEVEL_NAME);
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NopLogManagementCoordinator#createAppender(ManagedAppenderConfig).
     */
    @Test
    void testCreateAppender()
    throws LogManagementException {
        final ManagedAppenderConfig config = mock(ManagedAppenderConfig.class);
        instance.createAppender(config);
        then(engine).should().createAppender(config);
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NopLogManagementCoordinator#deleteAppender(String)}.
     */
    @Test
    void testDeleteAppender()
    throws LogManagementException {
        instance.deleteAppender(TEST_APPENDER_NAME);
        then(engine).should().deleteAppender(TEST_APPENDER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NopLogManagementCoordinator#attachAppender(String, String)}.
     */
    @Test
    void testAttachAppender()
    throws LogManagementException {
        instance.attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(engine).should().attachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NopLogManagementCoordinator#detachAppender(String, String)}.
     */
    @Test
    void testDetachAppender()
    throws LogManagementException {
        instance.detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(engine).should().detachAppender(TEST_LOGGER_NAME, TEST_APPENDER_NAME);
        then(engine).shouldHaveNoMoreInteractions();
    }
}
