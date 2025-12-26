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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Log management service that actually interacts with underlying logging
 * framework.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public interface LogManagementEngine {

    /**
     * Returns the supported logging levels.
     * 
     * @return The supported logging levels
     * @throws LogManagementException If an internal error occurs
     */
    List<Level> getLevels()
    throws LogManagementException;

    /**
     * Returns the root logger.
     * 
     * @return The root logger
     * @throws LogManagementException If an internal error occurs
     */
    Logger getRoot()
    throws LogManagementException;

    /**
     * Returns the logger with the specified name if instantiated,
     * {@code null} otherwise.
     * 
     * @param name The name of the logger
     * @return The logger, or {@code null}
     * @throws LogManagementException If an internal error occurs
     */
    Optional<Logger> findLogger(
            String name)
    throws LogManagementException;

    /**
     * Returns the logger with the specified name, creating it if missing.
     * 
     * @param name The name of the logger
     * @return The logger
     * @throws LogManagementException If an internal error occurs
     */
    Logger getLogger(
            String name)
    throws LogManagementException;

    /**
     * Returns direct child loggers of the specified parent logger.
     * <p>
     * Returns a map indexed by the final part of the name of the logger.
     * 
     * @param parent The parent logger name
     * @return The direct child loggers
     * @throws LoggerNotFoundException If the logger doesn't exist
     * @throws LogManagementException If an internal error occurs
     */
    Map<String, Logger> getChildren(
            String parent)
    throws LogManagementException;

    /**
     * Set the logging level for the logger with the specified name.
     * 
     * @param logger The name of the logger
     * @param level The code of the new level for the logger
     * @return The new status of the logger
     * @throws LoggerNotFoundException If the logger doesn't exist
     * @throws IllegalLevelException If the requested level is not available
     * or legal (f.e.: {@code null} level in root logger)
     * @throws LogManagementException If an internal error occurs
     */
    Logger setLevel(
            String logger,
            @Nullable String level)
    throws LogManagementException;

    /**
     * Returns the configuration of all the existing appenders.
     * <p>
     * The underlaying logging system can hide appenders created through other
     * methods (initial configuration, for example).
     * 
     * @return The existing appender configurations
     * @throws LogManagementException If an internal error occurs
     */
    List<Appender> getAppenders()
    throws LogManagementException;

    /**
     * Returns the name of the appenders attached to the specified logger.
     * 
     * @param logger The name of the logger
     * @return The names of the appenders attached to the logger
     * @throws LoggerNotFoundException If the logger doesn't exist
     * @throws LogManagementException If an internal error occurs
     */
    List<String> getAppenders(
            String logger)
    throws LogManagementException;

    /**
     * Returns the configuration of the specified managed appender if exists.
     * 
     * @param name The name of the appender
     * @return The appender configuration, if exists; {@code null} otherwise
     * @throws LogManagementException If an internal error occurs
     */
    Appender getAppender(
            String name)
    throws LogManagementException;

    /**
     * Creates a new appender with the specified configuration.
     * Properties set to {@code null} will use the default values.
     * <p>
     * Valid formats depend of the underlaying logging system.
     * 
     * @param config The appender configuration
     * @return The final configuration of the created appender
     * @throws InvalidAppenderConfigException If the appender configuration
     * is not valid
     * @throws LogManagementException If an internal error occurs
     */
    ManagedAppender createAppender(
            ManagedAppenderConfig config)
    throws LogManagementException;

    /**
     * Removes the specified appender, if exists.
     * 
     * @param name The name of the appender to remove
     * @return {@code true} if the appender was detached from any logger
     * @throws AppenderNotFoundException If the appender doesn't exist
     * @throws UnmanagedAppenderException If the appender cannot be modified
     * @throws LogManagementException If an internal error occurs
     */
    boolean deleteAppender(
            String name)
    throws LogManagementException;

    /**
     * Attach the existing specified appender to the specified logger.
     * 
     * @param logger The name of the logger to attach the appender to
     * @param appender The name of the target appender
     * @throws LoggerNotFoundException If the logger doesn't exist
     * @throws AppenderNotFoundException If the appender doesn't exist
     * @throws UnmanagedAppenderException If the appender cannot be modified
     * @throws LogManagementException If an internal error occurs
     */
    void attachAppender(
            String logger,
            String appender)
    throws LogManagementException;

    /**
     * Detach the existing specified appender from the specified logger.
     * 
     * @param logger The name of the logger to detach the appender from
     * @param appender The name of the target appender
     * @return If the appender was attached to the logger
     * @throws LoggerNotFoundException If the logger doesn't exist
     * @throws AppenderNotFoundException If the appender doesn't exist
     * @throws UnmanagedAppenderException If the appender cannot be modified
     * @throws LogManagementException If an internal error occurs
     */
    boolean detachAppender(
            String logger,
            String appender)
    throws LogManagementException;

    /**
     * Resets the logging system to its initial state.
     * 
     * @throws LogManagementException If an internal error occurs
     */
    void reset()
    throws LogManagementException;
}
