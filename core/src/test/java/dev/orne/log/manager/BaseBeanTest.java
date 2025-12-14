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

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

/**
 * Basic unit tests for library beans.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
abstract class BaseBeanTest<T> {

    /**
     * Creates the bean to be tested.
     * 
     * @return The bean created
     */
    protected abstract T createInstance();

    /**
     * Creates a copy bean to be tested.
     * 
     * @param copy The bean to copy, created with {@link #createInstance()}
     * @return The bean created
     */
    protected abstract T createCopy(T copy);

    /**
     * Test for {@link Object#hashCode()} and
     * {@link Object#equals(Object)} extensions.
     */
    @Test
    void testEqualsHashCodeCopy() {
        final T bean = createInstance();
        final T other = createCopy(bean);
        assertNotEquals(bean, (T) null);
        assertEquals(bean, bean);
        assertNotEquals(bean, new Object());
        assertEquals(bean, other);
        assertEquals(bean.hashCode(), other.hashCode());
    }

    /**
     * Test for {@link Object#toString()} extension.
     */
    @Test
    void testToString() {
        final T bean = createInstance();
        final String result = bean.toString();
        assertNotNull(result);
    }

    /**
     * Test for {@link Serializable} implementation.
     */
    @Test
    void testSerializable() {
        final T bean = createInstance();
        if (bean instanceof Serializable) {
            final byte[] data = SerializationUtils.serialize((Serializable) bean);
            final Object deserialized = SerializationUtils.deserialize(data);
            assertEquals(bean, deserialized);
        }
    }
}
