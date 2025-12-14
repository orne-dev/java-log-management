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

import org.apiguardian.api.API;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import dev.orne.log.manager.SizeBasedFileRollingPolicy;

/**
 * Logback implementation of a size based file rolling policy.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackSizeBasedFileRollingPolicy
implements LogbackFileRollingPolicy{

    /** The file rolling policy configuration. */
    private final SizeBasedFileRollingPolicy policyConfig;
    /** The Logback size based file rolling policy. */
    private final FixedWindowRollingPolicy rollingPolicy;
    /** The Logback size based file rolling triggering policy. */
    private final SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy;

    /**
     * Creates a new instance.
     * 
     * @param policyConfig The file rolling policy configuration
     * @param rollingPolicy The Logback rolling policy
     * @param triggeringPolicy The Logback triggering policy
     */
    public LogbackSizeBasedFileRollingPolicy(
            final SizeBasedFileRollingPolicy policyConfig,
            final FixedWindowRollingPolicy rollingPolicy,
            final SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy) {
        super();
        this.policyConfig = policyConfig;
        this.rollingPolicy = rollingPolicy;
        this.triggeringPolicy = triggeringPolicy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SizeBasedFileRollingPolicy getPolicyConfig() {
        return this.policyConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RollingPolicy getRollingPolicy() {
        return this.rollingPolicy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TriggeringPolicy<ILoggingEvent> getTriggeringPolicy() {
        return this.triggeringPolicy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.rollingPolicy.start();
        this.triggeringPolicy.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.triggeringPolicy.stop();
        this.rollingPolicy.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStarted() {
        return this.rollingPolicy.isStarted() && this.triggeringPolicy.isStarted();
    }
}
