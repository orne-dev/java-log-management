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

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@link ManagedAppender}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class ManagedAppenderTest extends BaseBeanTest<ManagedAppender> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ManagedAppender createInstance() {
        return Generators.randomValue(ManagedAppender.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ManagedAppender createCopy(
            final ManagedAppender bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the appender name is required.
     */
    @Test
    void testRequiredName() {
        final ManagedAppender.Builder builder = ManagedAppender.builder()
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8);
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the appender file name is required.
     */
    @Test
    void testRequiredFileName() {
        final ManagedAppender.Builder builder = ManagedAppender.builder()
                .withName("STDOUT")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8);
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the appender format is required.
     */
    @Test
    void testRequiredFormat() {
        final ManagedAppender.Builder builder = ManagedAppender.builder()
                .withName("STDOUT")
                .withFilename("out.log")
                .withCharset(StandardCharsets.UTF_8);
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the appender charset is required.
     */
    @Test
    void testRequiredCharset() {
        final ManagedAppender.Builder builder = ManagedAppender.builder()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n");
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the appender file rolling policy is optional.
     */
    @Test
    void testOptionalFileRollingPolicy() {
        final ManagedAppender bean = ManagedAppender.builder()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8)
                .build();
        assertFalse(bean.getFileRollingPolicy().isPresent());
    }

    /**
     * Tests that the appender builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final FileRollingPolicy policy = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final ManagedAppender bean = ManagedAppender.builder()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8)
                .withFileRollingPolicy(policy)
                .build();
        assertEquals("STDOUT", bean.getName());
        assertEquals("out.log", bean.getFilename());
        assertEquals("%m%n", bean.getFormat());
        assertEquals(StandardCharsets.UTF_8, bean.getCharset());
        assertTrue(bean.getFileRollingPolicy().isPresent());
        assertEquals(policy, bean.getFileRollingPolicy().get());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final FileRollingPolicy policy = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final FileRollingPolicy otherPolicy = SizeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L)
                .build();
        final ManagedAppender original = ManagedAppender.builder()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8)
                .withFileRollingPolicy(policy)
                .build();
        assertNotEquals(original, original.copy()
                .withName("OTHER_APPENDER")
                .build());
        assertNotEquals(original, original.copy()
                .withFilename("error.log")
                .build());
        assertNotEquals(original, original.copy()
                .withFormat(LogManagementConfig.Defaults.APPENDER_FALLBACK_FORMAT)
                .build());
        assertNotEquals(original, original.copy()
                .withCharset(StandardCharsets.ISO_8859_1)
                .build());
        assertNotEquals(original, original.copy()
                .withFileRollingPolicy(otherPolicy)
                .build());
        assertNotEquals(original, original.copy()
                .withFileRollingPolicy(null)
                .build());
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(ManagedAppender.class));
        assertNotNull(Generators.randomValue(ManagedAppender.class));
    }
}
