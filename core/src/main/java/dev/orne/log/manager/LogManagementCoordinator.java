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

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Log management coordinator that propagates log configuration changes
 * to all the coordinated JVM.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public interface LogManagementCoordinator {

    /**
     * Set the logging level for the logger with the specified name
     * in all the coordinated JVM.
     * 
     * @param logger The name of the logger
     * @param level The code of the new level for the logger
     * @throws LogManagementException If an error occurs coordinating the
     *         logging level change
     */
    void setLevel(
            String logger,
            @Nullable String level)
    throws LogManagementException;

    /**
     * Creates a new appender with the specified configuration
     * in all the coordinated JVM.
     * Properties set to {@code null} will use the default values.
     * <p>
     * Valid formats depend of the underlying logging system.
     * 
     * @param config The appender configuration
     * @throws InvalidAppenderConfigException If the appender configuration
     * is not valid
     * @throws LogManagementException If an internal error occurs
     */
    void createAppender(
            ManagedAppenderConfig config)
    throws LogManagementException;

    /**
     * Removes the specified appender in all the coordinated JVM, if exists.
     * 
     * @param name The name of the appender to remove
     * @throws AppenderNotFoundException If the appender doesn't exist
     * @throws UnmanagedAppenderException If the appender cannot be modified
     * @throws LogManagementException If an internal error occurs
     */
    void deleteAppender(
            String name)
    throws LogManagementException;

    /**
     * Attach the existing specified appender to the specified logger
     *  in all the coordinated JVM.
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
     * Detach the existing specified appender from the specified logger
     *  in all the coordinated JVM.
     * 
     * @param logger The name of the logger to detach the appender from
     * @param appender The name of the target appender
     * @throws LoggerNotFoundException If the logger doesn't exist
     * @throws AppenderNotFoundException If the appender doesn't exist
     * @throws UnmanagedAppenderException If the appender cannot be modified
     * @throws LogManagementException If an internal error occurs
     */
    void detachAppender(
            String logger,
            String appender)
    throws LogManagementException;
}
