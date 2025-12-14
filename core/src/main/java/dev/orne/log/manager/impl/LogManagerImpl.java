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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.log.manager.Appender;
import dev.orne.log.manager.Level;
import dev.orne.log.manager.LogManagementCoordinator;
import dev.orne.log.manager.LogManagementEngine;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.LogManager;
import dev.orne.log.manager.Logger;
import dev.orne.log.manager.ManagedAppenderConfig;

/**
 * Log management service implementation.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class LogManagerImpl
implements LogManager {

    /** The log management coordinator. */
    private final LogManagementCoordinator coordinator;
    /** The log management engine. */
    private final LogManagementEngine engine;

    /**
     * Creates a new instance.
     * 
     * @param coordinator The log management coordinator
     * @param engine The log management engine
     */
    public LogManagerImpl(
            final LogManagementCoordinator coordinator,
            final LogManagementEngine engine) {
        super();
        this.coordinator = coordinator;
        this.engine = engine;
    }

    /**
     * Returns the log management coordinator.
     * 
     * @return The log management coordinator
     */
    public LogManagementCoordinator getCoordinator() {
        return this.coordinator;
    }

    /**
     * Returns the log management engine.
     * 
     * @return The log management engine
     */
    public LogManagementEngine getEngine() {
        return this.engine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Level> getLevels()
    throws LogManagementException {
        return this.engine.getLevels();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getRoot()
    throws LogManagementException {
        return this.engine.getRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Logger> existsLogger(
            final String name)
    throws LogManagementException {
        return this.engine.findLogger(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger(
            final String name)
    throws LogManagementException {
        return this.engine.getLogger(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Logger> getChildren(
            final String parent)
    throws LogManagementException {
        return this.engine.getChildren(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(
            final String logger,
            final @Nullable String level)
    throws LogManagementException {
        this.coordinator.setLevel(logger, level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appender> getAppenders()
    throws LogManagementException {
        return this.engine.getAppenders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAppenders(
            final String name)
    throws LogManagementException {
        return this.engine.getAppenders(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Appender getAppender(
            final String name)
    throws LogManagementException {
        return this.engine.getAppender(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAppender(
            final ManagedAppenderConfig config)
    throws LogManagementException {
        this.coordinator.createAppender(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAppender(
            final String name)
    throws LogManagementException {
        this.coordinator.deleteAppender(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        this.coordinator.attachAppender(logger, appender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        this.coordinator.detachAppender(logger, appender);
    }
}
