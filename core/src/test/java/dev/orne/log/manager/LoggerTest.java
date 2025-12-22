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
 * Unit tests for {@link Logger}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LoggerTest extends BaseBeanTest<Logger> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger createInstance() {
        return Generators.randomValue(Logger.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger createCopy(
            final Logger bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the logger name is required.
     */
    @Test
    void testRequiredName() {
        final Logger.Builder builder = Logger.builder()
                .withLevel(Generators.randomValue(Level.class))
                .withLevelInherited(Generators.randomValue(boolean.class));
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the logger level is required.
     */
    @Test
    void testRequiredLevel() {
        final Logger.Builder builder = Logger.builder()
                .withName(Generators.randomValue(String.class))
                .withLevelInherited(Generators.randomValue(boolean.class));
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the logger level heritage flag defaults to true.
     */
    @Test
    void testRequiredLevelInherited() {
        final Logger.Builder builder = Logger.builder()
                .withLevel(Generators.randomValue(Level.class))
                .withName(Generators.randomValue(String.class));
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the logger builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final Level level = Generators.randomValue(Level.class);
        final Logger bean = Logger.builder()
                .withName("LOGGER")
                .withLevel(level)
                .withLevelInherited(false)
                .build();
        assertEquals("LOGGER", bean.getName());
        assertEquals(level, bean.getLevel());
        assertFalse(bean.isLevelInherited());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final Level level = Level.builder()
                .withName("LEVEL")
                .withValue(100)
                .build();
        final Level otherLevel = level.copy()
                .withValue(200)
                .build();
        final Logger original = Logger.builder()
                .withName("LOGGER")
                .withLevel(level)
                .withLevelInherited(true)
                .build();
        assertNotEquals(original, original.copy()
                .withName("OTHER_LEVEL")
                .build());
        assertNotEquals(original, original.copy()
                .withLevel(otherLevel)
                .build());
        assertNotEquals(original, original.copy()
                .withLevelInherited(false)
                .build());
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(Logger.class));
        assertNotNull(Generators.randomValue(Logger.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Test
    void testToString() {
        super.testToString();
        assertNotNull(createInstance().copy()
                .withLevelInherited(false)
                .build()
                .toString());
        assertNotNull(createInstance().copy()
                .withLevelInherited(true)
                .build()
                .toString());
    }
}
