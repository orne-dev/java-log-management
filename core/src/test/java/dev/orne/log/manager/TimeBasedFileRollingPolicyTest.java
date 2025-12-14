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
 * Unit tests for {@link TimeBasedFileRollingPolicy}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class TimeBasedFileRollingPolicyTest extends BaseBeanTest<TimeBasedFileRollingPolicy> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected TimeBasedFileRollingPolicy createInstance() {
        return Generators.randomValue(TimeBasedFileRollingPolicy.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TimeBasedFileRollingPolicy createCopy(
            final TimeBasedFileRollingPolicy bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the policy period is required.
     */
    @Test
    void testRequiredPeriod() {
        final TimeBasedFileRollingPolicy.Builder builder = TimeBasedFileRollingPolicy.builder();
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the policy max history is optional.
     */
    @Test
    void testOptionalMaxHistory() {
        final TimeBasedFileRollingPolicy bean = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        assertEquals(FileRollingPolicy.UNBOUNDED_MAX_HISTORY, bean.getMaxHistory());
    }

    /**
     * Tests that the policy max history size is optional.
     */
    @Test
    void testOptionalMaxHistorySize() {
        TimeBasedFileRollingPolicy bean = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        assertTrue(bean.getMaxHistorySize().isEmpty());
        bean = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
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
        final TimeBasedFileRollingPolicy bean = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        assertFalse(bean.isCompressed());
    }

    /**
     * Tests that the level builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final TimeBasedFileRollingPolicy bean = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.WEEKLY)
                .withMaxHistory(8)
                .withMaxHistorySize(10L * 1024L * 1024L)
                .withCompressed(true)
                .build();
        assertEquals(RollingPeriod.WEEKLY, bean.getPeriod());
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
        final TimeBasedFileRollingPolicy original = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
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
        assertNotNull(Generators.defaultValue(TimeBasedFileRollingPolicy.class));
        assertNotNull(Generators.randomValue(TimeBasedFileRollingPolicy.class));
    }
}
