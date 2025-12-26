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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Utility methods for testing.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
public final class TestUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private TestUtils() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated");
    }

    /**
     * Asserts that the given class is a well-formed utility class.
     * 
     * @param clazz The class to check
     * @see https://stackoverflow.com/a/10872497
     */
    public static void assertUtilityClass(
            final Class<?> clazz) {
        assertTrue(
                Modifier.isFinal(clazz.getModifiers()),
                "Class must be final");
        assertEquals(
                1,
                clazz.getDeclaredConstructors().length,
                "There must be only one constructor");
        final Constructor<?> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (final NoSuchMethodException e) {
            throw new AssertionError(
                    "The class must override default constructor", e);
        }
        if (constructor.canAccess(null) ||
                    !Modifier.isPrivate(constructor.getModifiers())) {
            fail("Constructor must be private");
        }
        constructor.setAccessible(true);
        assertThrows(
                Exception.class,
                constructor::newInstance);
        constructor.setAccessible(false);
        for (final Field field : clazz.getFields()) {
            if (!Modifier.isStatic(field.getModifiers())
                    && field.getDeclaringClass().equals(clazz)) {
                fail("Class cannot contain non-static fields:" + field);
            }
        }
        for (final Method method : clazz.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())
                    && method.getDeclaringClass().equals(clazz)) {
                fail("Class cannot contain non-static methods:" + method);
            }
        }
    }
}
