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
 * Unit tests for {@link Level}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LevelTest extends BaseBeanTest<Level> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Level createInstance() {
        return Generators.randomValue(Level.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Level createCopy(
            final Level bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the level name is required.
     */
    @Test
    void testRequiredName() {
        final Level.Builder builder = Level.builder()
                .withValue(100);
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the level value is required.
     */
    @Test
    void testRequiredValue() {
        final Level.Builder builder = Level.builder()
                .withName("LEVEL");
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the level builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final Level bean = Level.builder()
                .withName("LEVEL")
                .withValue(100)
                .build();
        assertEquals("LEVEL", bean.getName());
        assertEquals(100, bean.getValue());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    void testCompareTo() {
        final Level level1 = Level.builder()
                .withName("LEVEL1")
                .withValue(100)
                .build();
        final Level level2 = Level.builder()
                .withName("LEVEL2")
                .withValue(200)
                .build();
        assertTrue(level1.compareTo(level2) < 0);
        assertTrue(level2.compareTo(level1) > 0);
        assertEquals(0, level1.compareTo(level1));
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final Level original = Level.builder()
                .withName("LEVEL")
                .withValue(100)
                .build();
        assertNotEquals(original, original.copy()
                .withName("OTHER_LEVEL")
                .build());
        assertNotEquals(original, original.copy()
                .withValue(200)
                .build());
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(Level.class));
        assertNotNull(Generators.randomValue(Level.class));
    }
}
