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

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@link SizeBasedFileRollingPolicy}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class SizeBasedFileRollingPolicyTest extends BaseBeanTest<SizeBasedFileRollingPolicy> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected SizeBasedFileRollingPolicy createInstance() {
        return Generators.randomValue(SizeBasedFileRollingPolicy.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SizeBasedFileRollingPolicy createCopy(
            final SizeBasedFileRollingPolicy bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the policy max size name is required.
     */
    @Test
    void testRequiredFileSize() {
        final SizeBasedFileRollingPolicy.Builder builder = SizeBasedFileRollingPolicy.builder();
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the policy max history is optional.
     */
    @Test
    void testOptionalMaxHistory() {
        final SizeBasedFileRollingPolicy bean = SizeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertEquals(FileRollingPolicy.UNBOUNDED_MAX_HISTORY, bean.getMaxHistory());
    }

    /**
     * Tests that the policy compression flag is optional.
     */
    @Test
    void testOptionalCompressed() {
        final SizeBasedFileRollingPolicy bean = SizeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertFalse(bean.isCompressed());
    }

    /**
     * Tests that the policy builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final SizeBasedFileRollingPolicy bean = SizeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L)
                .withMaxHistory(5)
                .withCompressed(true)
                .build();
        assertEquals(10L * 1024L * 1024L, bean.getFileSize());
        assertEquals(5, bean.getMaxHistory());
        assertTrue(bean.isCompressed());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final SizeBasedFileRollingPolicy original = SizeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertNotEquals(original, original.copy()
                .withFileSize(50L * 1024L * 1024L)
                .build());
        assertNotEquals(original, original.copy()
                .withMaxHistory(20)
                .build());
        assertNotEquals(original, original.copy()
                .withCompressed(true)
                .build());
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(SizeBasedFileRollingPolicy.class));
        assertNotNull(Generators.randomValue(SizeBasedFileRollingPolicy.class));
    }
}
