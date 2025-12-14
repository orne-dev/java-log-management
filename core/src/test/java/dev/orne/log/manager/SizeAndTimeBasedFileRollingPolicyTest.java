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
 * Unit tests for {@link SizeAndTimeBasedFileRollingPolicy}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class SizeAndTimeBasedFileRollingPolicyTest extends BaseBeanTest<SizeAndTimeBasedFileRollingPolicy> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected SizeAndTimeBasedFileRollingPolicy createInstance() {
        return Generators.randomValue(SizeAndTimeBasedFileRollingPolicy.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SizeAndTimeBasedFileRollingPolicy createCopy(
            final SizeAndTimeBasedFileRollingPolicy bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the policy period is required.
     */
    @Test
    void testRequiredPeriod() {
        final SizeAndTimeBasedFileRollingPolicy.Builder builder = SizeAndTimeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L);
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the policy max file size is required.
     */
    @Test
    void testRequiredFileSize() {
        final SizeAndTimeBasedFileRollingPolicy.Builder builder = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY);
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the policy max history is optional.
     */
    @Test
    void testOptionalMaxHistory() {
        final SizeAndTimeBasedFileRollingPolicy bean = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertEquals(FileRollingPolicy.UNBOUNDED_MAX_HISTORY, bean.getMaxHistory());
    }

    /**
     * Tests that the policy max history size is optional.
     */
    @Test
    void testOptionalMaxHistorySize() {
        SizeAndTimeBasedFileRollingPolicy bean = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertTrue(bean.getMaxHistorySize().isEmpty());
        bean = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .withFileSize(10L * 1024L * 1024L)
                .withMaxHistorySize(10L * 1024L * 1024L)
                .build()
                .copy()
                .withMaxHistorySize(null)
                .build();
        assertTrue(bean.getMaxHistorySize().isEmpty());
    }

    /**
     * Tests that the policy compression flag is optional.
     */
    @Test
    void testOptionalCompressed() {
        final SizeAndTimeBasedFileRollingPolicy bean = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertFalse(bean.isCompressed());
    }

    /**
     * Tests that the level builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final SizeAndTimeBasedFileRollingPolicy bean = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.WEEKLY)
                .withFileSize(10L * 1024L * 1024L)
                .withMaxHistory(8)
                .withMaxHistorySize(10L * 1024L * 1024L)
                .withCompressed(true)
                .build();
        assertEquals(RollingPeriod.WEEKLY, bean.getPeriod());
        assertEquals(10L * 1024L * 1024L, bean.getFileSize());
        assertEquals(8, bean.getMaxHistory());
        assertTrue(bean.getMaxHistorySize().isPresent());
        assertEquals(10L * 1024L * 1024L, bean.getMaxHistorySize().getAsLong());
        assertTrue(bean.isCompressed());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final SizeAndTimeBasedFileRollingPolicy original = SizeAndTimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .withFileSize(10L * 1024L * 1024L)
                .build();
        assertNotEquals(original, original.copy()
                .withFileSize(50L * 1024L * 1024L)
                .build());
        assertNotEquals(original, original.copy()
                .withPeriod(RollingPeriod.WEEKLY)
                .build());
        assertNotEquals(original, original.copy()
                .withMaxHistory(20)
                .build());
        assertNotEquals(original, original.copy()
                .withMaxHistorySize(50L * 1024L * 1024L)
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
        assertNotNull(Generators.defaultValue(SizeAndTimeBasedFileRollingPolicy.class));
        assertNotNull(Generators.randomValue(SizeAndTimeBasedFileRollingPolicy.class));
    }
}
