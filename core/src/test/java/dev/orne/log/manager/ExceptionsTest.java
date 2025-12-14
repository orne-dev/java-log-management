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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for library exceptions.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 * @see LogManagementException
 * @see IllegalLevelException
 * @see LoggerNotFoundException
 * @see AppenderNotFoundException
 * @see UnmanagedAppenderException
 * @see InvalidAppenderConfigException
 * @see InvalidFileRollingConfigException
 * @see UnsupportedFileRollingPolicyException
 */
@Tag("ut")
class ExceptionsTest {

    /** Message for exception testing. */
    private static final String TEST_MESSAGE = "Test message";
    /** Cause for exception testing. */
    private static final Throwable TEST_CAUSE = new Exception();

    /**
     * Test for {@link LogManagementException}.
     */
    @Test
    void testLogManagementException() {
        assertEmptyException(new LogManagementException());
        assertMessageException(new LogManagementException(TEST_MESSAGE));
        assertCauseException(new LogManagementException(TEST_CAUSE));
        assertFullException(new LogManagementException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link IllegalLevelException}.
     */
    @Test
    void testIllegalLevelException() {
        assertEmptyException(new IllegalLevelException());
        assertMessageException(new IllegalLevelException(TEST_MESSAGE));
        assertCauseException(new IllegalLevelException(TEST_CAUSE));
        assertFullException(new IllegalLevelException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link LoggerNotFoundException}.
     */
    @Test
    void testLoggerNotFoundException() {
        assertEmptyException(new LoggerNotFoundException());
        assertMessageException(new LoggerNotFoundException(TEST_MESSAGE));
        assertCauseException(new LoggerNotFoundException(TEST_CAUSE));
        assertFullException(new LoggerNotFoundException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link AppenderNotFoundException}.
     */
    @Test
    void testAppenderNotFoundException() {
        assertEmptyException(new AppenderNotFoundException());
        assertMessageException(new AppenderNotFoundException(TEST_MESSAGE));
        assertCauseException(new AppenderNotFoundException(TEST_CAUSE));
        assertFullException(new AppenderNotFoundException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link UnmanagedAppenderException}.
     */
    @Test
    void testUnmanagedAppenderException() {
        assertEmptyException(new UnmanagedAppenderException());
        assertMessageException(new UnmanagedAppenderException(TEST_MESSAGE));
        assertCauseException(new UnmanagedAppenderException(TEST_CAUSE));
        assertFullException(new UnmanagedAppenderException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link InvalidAppenderConfigException}.
     */
    @Test
    void testInvalidAppenderConfigException() {
        assertEmptyException(new InvalidAppenderConfigException());
        assertMessageException(new InvalidAppenderConfigException(TEST_MESSAGE));
        assertCauseException(new InvalidAppenderConfigException(TEST_CAUSE));
        assertFullException(new InvalidAppenderConfigException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link InvalidFileRollingConfigException}.
     */
    @Test
    void testInvalidFileRollingConfigException() {
        assertEmptyException(new InvalidFileRollingConfigException());
        assertMessageException(new InvalidFileRollingConfigException(TEST_MESSAGE));
        assertCauseException(new InvalidFileRollingConfigException(TEST_CAUSE));
        assertFullException(new InvalidFileRollingConfigException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Test for {@link UnsupportedFileRollingPolicyException}.
     */
    @Test
    void testUnsupportedFileRollingPolicyException() {
        assertEmptyException(new UnsupportedFileRollingPolicyException());
        assertMessageException(new UnsupportedFileRollingPolicyException(TEST_MESSAGE));
        assertCauseException(new UnsupportedFileRollingPolicyException(TEST_CAUSE));
        assertFullException(new UnsupportedFileRollingPolicyException(TEST_MESSAGE, TEST_CAUSE));
    }

    /**
     * Asserts that exception has no message and no cause.
     * 
     * @param exception The exception to test
     */
    private void assertEmptyException(
            final Exception exception) {
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Asserts that exception has message but no cause.
     * 
     * @param exception The exception to test
     */
    private void assertMessageException(
            final Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Asserts that exception has cause but no message.
     * 
     * @param exception The exception to test
     */
    private void assertCauseException(
            final Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_CAUSE.toString(), exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
    }

    /**
     * Asserts that exception has message and cause.
     * 
     * @param exception The exception to test
     */
    private void assertFullException(
            final Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
    }
}
