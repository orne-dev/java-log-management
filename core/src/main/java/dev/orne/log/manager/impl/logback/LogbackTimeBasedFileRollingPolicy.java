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
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import dev.orne.log.manager.TimeBasedFileRollingPolicy;

/**
 * Logback implementation of a time based file rolling policy.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackTimeBasedFileRollingPolicy
implements LogbackFileRollingPolicy{

    /** The file rolling policy configuration. */
    private final TimeBasedFileRollingPolicy policyConfig;
    /** The Logback rolling policy. */
    private final TimeBasedRollingPolicy<ILoggingEvent> policy;

    /**
     * Creates a new instance.
     * 
     * @param policyConfig The file rolling policy configuration
     * @param policy The Logback rolling policy
     */
    public LogbackTimeBasedFileRollingPolicy(
            final TimeBasedFileRollingPolicy policyConfig,
            final TimeBasedRollingPolicy<ILoggingEvent> policy) {
        super();
        this.policyConfig = policyConfig;
        this.policy = policy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeBasedFileRollingPolicy getPolicyConfig() {
        return this.policyConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RollingPolicy getRollingPolicy() {
        return this.policy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TriggeringPolicy<ILoggingEvent> getTriggeringPolicy() {
        return this.policy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.policy.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.policy.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStarted() {
        return this.policy.isStarted();
    }
}
