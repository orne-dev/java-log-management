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

import java.util.Objects;

import org.apiguardian.api.API;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import dev.orne.log.manager.ManagedAppender;

/**
 * Logback based implementation of {@code ManagedAppender}.
 * <p>
 * This class is final and immutable and intended for instantiation from
 * {@code LogbackAppenderFactory} instances.
 * Extends {@code ManagedAppender} to inherit properties from its
 * super class.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackManagedAppender
implements LogbackAppender {

    /** The managed appender configuration. */
    private final ManagedAppender appenderConfig;
    /** The Logback appender. */
    private final Appender<ILoggingEvent> appender;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected LogbackManagedAppender(
            final ManagedAppender appenderConfig,
            final Appender<ILoggingEvent> appender) {
        super();
        this.appenderConfig = Objects.requireNonNull(
                appenderConfig, 
                "The appender configuration cannot be null");
        this.appender = Objects.requireNonNull(
                appender, 
                "The Logback appender cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagedAppender getData() {
        return this.appenderConfig;
    }

    /**
     * Returns the Logback appender.
     * 
     * @return The Logback appender
     */
    public Appender<ILoggingEvent> getAppender() {
        return this.appender;
    }
}
