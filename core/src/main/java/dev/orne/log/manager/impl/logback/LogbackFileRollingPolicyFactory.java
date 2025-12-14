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

import ch.qos.logback.classic.LoggerContext;
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;

/**
 * Factory for creating Logback file rolling policies.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public interface LogbackFileRollingPolicyFactory {

    /**
     * Returns {@code true} if this factory supports the specified
     * policy configuration type.
     * 
     * @param type The file rolling policy configuration type
     * @return If this factory supports the specified policy configuration type
     */
    boolean supports(
            Class<? extends FileRollingPolicy> type);

    /**
     * Creates a new file rolling policy based on the appender configuration.
     * 
     * @param context The Logback context
     * @param config The appender configuration
     * @return The new file rolling policy
     * @throws LogManagementException If an error occurs
     * @throws InvalidAppenderConfigException If the appender configuration
     * is not valid
     */
    LogbackFileRollingPolicy create(
            LoggerContext context,
            ManagedAppender config)
    throws LogManagementException;

    /**
     * Provider for SPI based loading of Logback file rolling policy factories.
     * <p>
     * Allows applying configuration when creating the factory.
     * <p>
     * Register through SPI by adding a file named
     * {@code dev.orne.log.manager.logback.LogbackFileRollingPolicyFactory$Provider}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    interface Provider {

        /**
         * Creates a configured Logback file rolling policy factory.
         * 
         * @param config The log management configuration
         * @return The configured Logback file rolling policy factory
         * @throws InvalidFileRollingConfigException If error occurs configuring
         * the factory
         */
        LogbackFileRollingPolicyFactory create(
                LogManagementConfig config);
    }
}
