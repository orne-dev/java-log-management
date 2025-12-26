package dev.orne.log.manager.impl;

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
import org.jspecify.annotations.Nullable;

import dev.orne.log.manager.LogManagementCoordinator;
import dev.orne.log.manager.LogManagementEngine;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppenderConfig;

/**
 * Default implementation of log management coordinator that only
 * changes log configuration in current JVM.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class NopLogManagementCoordinator
implements LogManagementCoordinator {

    /** The log management engine. */
    private final LogManagementEngine engine;

    /**
     * Creates a new instance.
     * 
     * @param engine The log management engine
     */
    public NopLogManagementCoordinator(
            final LogManagementEngine engine) {
        super();
        this.engine = engine;
    }

    /**
     * Returns the log management engine.
     * 
     * @return The log management engine
     */
    protected LogManagementEngine getEngine() {
        return this.engine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(
            final String logger,
            final @Nullable String level)
    throws LogManagementException {
        this.engine.setLevel(logger, level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAppender(
            final ManagedAppenderConfig config)
    throws LogManagementException {
        this.engine.createAppender(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAppender(
            final String name)
    throws LogManagementException {
        this.engine.deleteAppender(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        this.engine.attachAppender(logger, appender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        this.engine.detachAppender(logger, appender);
    }
}
