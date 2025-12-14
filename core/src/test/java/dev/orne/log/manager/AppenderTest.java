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
 * Unit tests for {@link Appender}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class AppenderTest extends BaseBeanTest<Appender> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Appender createInstance() {
        return Generators.randomValue(Appender.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Appender createCopy(
            final Appender bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the Appender name is required.
     */
    @Test
    void testRequiredName() {
        final Appender.Builder builder = Appender.builder();
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the Appender builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final Appender bean = Appender.builder()
                .withName("CONSOLE")
                .build();
        assertEquals("CONSOLE", bean.getName());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final Appender original = Appender.builder()
                .withName("APPENDER")
                .build();
        assertNotEquals(original, original.copy()
                .withName("OTHER_APPENDER")
                .build());
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(Appender.class));
        assertNotNull(Generators.randomValue(Appender.class));
    }
}
