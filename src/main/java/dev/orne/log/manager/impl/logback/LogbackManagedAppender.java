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
import java.util.Optional;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.LifeCycle;
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
implements LogbackAppender, LifeCycle {

    /** The managed appender configuration. */
    private final ManagedAppender appenderConfig;
    /** The Logback appender. */
    private final Appender<ILoggingEvent> appender;
    /** The Logback encoder of the appender. */
    private final Encoder<ILoggingEvent> encoder;
    /** The Logback file rolling policy of the appender. */
    private final @Nullable LogbackFileRollingPolicy rollingPolicy;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected LogbackManagedAppender(
            final ManagedAppender appenderConfig,
            final Appender<ILoggingEvent> appender,
            final Encoder<ILoggingEvent> encoder,
            final @Nullable LogbackFileRollingPolicy rollingPolicy) {
        super();
        this.appenderConfig = Objects.requireNonNull(
                appenderConfig, 
                "The appender configuration cannot be null");
        this.appender = Objects.requireNonNull(
                appender, 
                "The Logback appender cannot be null");
        this.encoder = Objects.requireNonNull(
                encoder,
                "The Logback encoder cannot be null");
        this.rollingPolicy = rollingPolicy;
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

    /**
     * Returns the Logback encoder of the appender.
     * 
     * @return The Logback encoder of the appender
     */
    public Encoder<ILoggingEvent> getEncoder() {
        return this.encoder;
    }

    /**
     * Returns the Logback file rolling policy of the appender.
     * 
     * @return The Logback file rolling policy of the appender
     */
    public Optional<LogbackFileRollingPolicy> getRollingPolicy() {
        return Optional.ofNullable(this.rollingPolicy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.encoder.start();
        if (this.rollingPolicy != null) {
            this.rollingPolicy.start();
        }
        this.appender.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStarted() {
        return this.appender.isStarted()
                && this.encoder.isStarted()
                && (this.rollingPolicy == null || this.rollingPolicy.isStarted());
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        this.appender.stop();
        if (this.rollingPolicy != null) {
            this.rollingPolicy.stop();
        }
        this.encoder.stop();
    }
}
