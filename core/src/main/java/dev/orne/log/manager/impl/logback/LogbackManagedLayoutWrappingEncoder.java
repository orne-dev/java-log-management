package dev.orne.log.manager.impl.logback;

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

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

/**
 * Logback based managed layout wrapping encoder.
 * <p>
 * This class is final and immutable and intended for instantiation from
 * {@code LogbackEncoderFactory} instances.
 * Extends {@code LayoutWrappingEncoder} to inherit properties from its
 * super class.
 * <p>
 * All nested components of this appender are managed, so the class must
 * propagate all life-cycle operations to all the nested components.
 * 
 * @param <E> The type of log event
 */
public class LogbackManagedLayoutWrappingEncoder<E>
extends LayoutWrappingEncoder<E> {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void start() {
        getLayout().start();
        super.start();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
        getLayout().stop();
    }
}
