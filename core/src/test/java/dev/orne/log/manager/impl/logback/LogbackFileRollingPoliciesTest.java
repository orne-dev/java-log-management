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
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.SizeAndTimeBasedFileRollingPolicy;
import dev.orne.log.manager.SizeBasedFileRollingPolicy;
import dev.orne.log.manager.TestUtils;
import dev.orne.log.manager.TimeBasedFileRollingPolicy;
import dev.orne.log.manager.UnsupportedFileRollingPolicyException;

/**
 * Unit tests for {@link LogbackFileRollingPolicies}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2026-02
 * @since 1.0
 */
@Tag("ut")
class LogbackFileRollingPoliciesTest {

    /**
     * Test for {@link LogbackFileRollingPolicies#LogbackFileRollingPolicies(LogManagementConfig)}.
     */
    @Test
    void givenConfig_whenConfigConstructor_willLoadFactoriesFromSpi()
    throws LogManagementException {
        assertThrows(
                NullPointerException.class,
                () -> new LogbackFileRollingPolicies((LogManagementConfig) null));
        final LogManagementConfig config = spy(LogManagementConfig.class);
        final LogbackFileRollingPolicies policies = new LogbackFileRollingPolicies(config);
        assertNotNull(policies.getFactories());
        assertNotNull(policies.getCache());
        assertTrue(policies.getCache().isEmpty());
        assertTrue(policies.getFactories()
                .stream()
                .filter(LogbackTimeBasedFileRollingPolicyFactory.class::isInstance)
                .findAny()
                .isPresent());
        assertTrue(policies.getFactories()
                .stream()
                .filter(LogbackSizeBasedFileRollingPolicyFactory.class::isInstance)
                .findAny()
                .isPresent());
        assertTrue(policies.getFactories()
                .stream()
                .filter(LogbackSizeAndTimeBasedFileRollingPolicyFactory.class::isInstance)
                .findAny()
                .isPresent());
        assertNotNull(
                policies.getFactory(TimeBasedFileRollingPolicy.class));
        assertNotNull(
                policies.getFactory(SizeBasedFileRollingPolicy.class));
        assertNotNull(
                policies.getFactory(SizeAndTimeBasedFileRollingPolicy.class));
        assertThrows(
                UnsupportedFileRollingPolicyException.class,
                () -> policies.getFactory(MockPolicy.class));
        assertFalse(policies.getCache().isEmpty());
    }

    /**
     * Test for {@link LogbackFileRollingPolicies#LogbackFileRollingPolicies(LogbackFileRollingPolicyFactory...)}.
     */
    @Test
    void givenCustomFactories_whenFactoriesConstructor_willUseGivenFactories()
    throws LogManagementException {
        final MockFactory factory = spy(new MockFactory());
        final LogbackFileRollingPolicies policies = new LogbackFileRollingPolicies(
                factory);
        assertNotNull(policies.getFactories());
        assertEquals(1, policies.getFactories().size());
        assertTrue(policies.getFactories().contains(factory));
        assertNotNull(policies.getCache());
        assertTrue(policies.getCache().isEmpty());
        assertThrows(
                UnsupportedFileRollingPolicyException.class,
                () -> policies.getFactory(TimeBasedFileRollingPolicy.class));
        assertThrows(
                UnsupportedFileRollingPolicyException.class,
                () -> policies.getFactory(SizeBasedFileRollingPolicy.class));
        assertThrows(
                UnsupportedFileRollingPolicyException.class,
                () -> policies.getFactory(SizeAndTimeBasedFileRollingPolicy.class));
        assertSame(factory, policies.getFactory(MockPolicy.class));
        assertFalse(policies.getCache().isEmpty());
    }

    /**
     * Test for {@link LogbackFileRollingPolicies#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenNoPolicy_whenCreate_willReturnNull()
    throws LogManagementException {
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender config = mock(ManagedAppender.class);
        given(config.getFileRollingPolicy()).willReturn(Optional.empty());
        final MockFactory factory = spy(new MockFactory());
        final LogbackFileRollingPolicies policies = new LogbackFileRollingPolicies(
                factory);
        assertNull(policies.create(context, config));
    }

    /**
     * Test for {@link LogbackFileRollingPolicies#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenSupportedPolicy_whenCreate_willCallFactory()
    throws LogManagementException {
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender config = mock(ManagedAppender.class);
        final MockPolicy mockPolicyConfig = mock(MockPolicy.class);
        given(config.getFileRollingPolicy()).willReturn(Optional.of(mockPolicyConfig));
        final MockFactory factory = spy(new MockFactory());
        final LogbackFileRollingPolicy mockResult = mock(LogbackFileRollingPolicy.class);
        willReturn(mockResult).given(factory).create(context, config);
        final LogbackFileRollingPolicies policies = new LogbackFileRollingPolicies(
                factory);
        final LogbackFileRollingPolicy result = policies.create(context, config);
        assertSame(mockResult, result);
    }

    /**
     * Test for {@link LogbackFileRollingPolicies#create(LoggerContext, ManagedAppender)}.
     */
    @Test
    void givenUnsupportedPolicy_whenCreate_willThrowException() {
        final LoggerContext context = mock(LoggerContext.class);
        final ManagedAppender config = mock(ManagedAppender.class);
        final SizeBasedFileRollingPolicy policyConfig = SizeBasedFileRollingPolicy.builder()
                .withFileSize(1000L)
                .build();
        given(config.getFileRollingPolicy()).willReturn(Optional.of(policyConfig));
        final MockFactory factory = spy(new MockFactory());
        final LogbackFileRollingPolicies policies = new LogbackFileRollingPolicies(
                factory);
        assertThrows(
                UnsupportedFileRollingPolicyException.class,
                () -> policies.create(context, config));
    }

    /**
     * Test for protected extension points.
     * 
     * @see LogbackFileRollingPolicies#getFactories()
     * @see LogbackFileRollingPolicies#getCache()
     */
    @Test
    void testExtensionPoints() {
        TestUtils.assertProtectedMethod(
                LogbackFileRollingPolicies.class,
                "getFactories");
        TestUtils.assertProtectedMethod(
                LogbackFileRollingPolicies.class,
                "getCache");
    }

    static class MockPolicy
    extends FileRollingPolicy {

        private static final long serialVersionUID = 1L;

        public MockPolicy() {
            this(new BuilderImpl());
        }

        public MockPolicy(BuilderImpl builder) {
            super(builder);
        }

        @Override
        public BuilderImpl copy() {
            return new BuilderImpl();
        }

        static class BuilderImpl
        extends FileRollingPolicy.BuilderImpl {

            @Override
            public MockPolicy build() {
                return new MockPolicy(this);
            }
        }
    }

    static class MockFactory
    implements LogbackFileRollingPolicyFactory {

        @Override
        public boolean supports(
                final Class<? extends FileRollingPolicy> type) {
            return MockPolicy.class.isAssignableFrom(type);
        }

        @Override
        public LogbackFileRollingPolicy create(
                final LoggerContext context,
                final ManagedAppender config)
        throws LogManagementException {
            throw new UnsupportedOperationException("Requires mocking");
        }
    }
}
