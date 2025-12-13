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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import ch.qos.logback.classic.LoggerContext;
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.UnsupportedFileRollingPolicyException;

/**
 * Registry of available Logback file rolling policy factories.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackFileRollingPolicies {

    /** The available file rolling policy factories. */
    private final List<LogbackFileRollingPolicyFactory> factories;
    /** The cached file rolling policy factories, by configuration type. */
    private final Map<Class<? extends FileRollingPolicy>, LogbackFileRollingPolicyFactory> cache =
            new HashMap<>();

    /**
     * Creates a new instance, loading the registered factories from the SPI.
     * 
     * @param config The log management configuration
     */
    public LogbackFileRollingPolicies(
            final LogManagementConfig config) {
        this(loadSpiFactories(config));
    }

    /**
     * Creates a new instance with the specified file rolling policy
     * factories.
     * 
     * @param factories The registered factories
     */
    public LogbackFileRollingPolicies(
            final LogbackFileRollingPolicyFactory... factories) {
        this(List.of(factories));
    }

    /**
     * Creates a new instance with the specified file rolling policy
     * factories.
     * 
     * @param factories The registered factories
     */
    public LogbackFileRollingPolicies(
            final List<LogbackFileRollingPolicyFactory> factories) {
        super();
        this.factories = List.copyOf(factories);
    }

    /**
     * Returns the available file rolling policy factories.
     * 
     * @return The available file rolling policy factories
     */
    protected List<LogbackFileRollingPolicyFactory> getFactories() {
        return this.factories;
    }

    /**
     * Returns the cached file rolling policy factories, by configuration type.
     * 
     * @return The cached file rolling policy factories, by configuration type
     */
    protected Map<Class<? extends FileRollingPolicy>, LogbackFileRollingPolicyFactory> getCache() {
        return this.cache;
    }

    /**
     * Clears the file rolling policy factories by configuration type cache.
     */
    public synchronized void clearCache() {
        this.cache.clear();
    }

    /**
     * Returns a file rolling policy factory that supports the specified
     * configuration type.
     * 
     * @param type The file rolling policy configuration type
     * @return The file rolling policy factory
     * @throws UnsupportedFileRollingPolicyException If no factory supports the
     * requested configuration type
     * @throws LogManagementException If an unexpected error occurs
     */
    public synchronized LogbackFileRollingPolicyFactory getFactory(
            final Class<? extends FileRollingPolicy> type)
    throws LogManagementException {
        Objects.requireNonNull(type);
        LogbackFileRollingPolicyFactory result = this.cache.computeIfAbsent(type, key -> {
            for (final LogbackFileRollingPolicyFactory factory : this.factories) {
                if (factory.supports(type)) {
                    return factory;
                }
            }
            return null;
        });
        if (result == null) {
            throw new UnsupportedFileRollingPolicyException(String.format(
                    "No file rolling policy factory for class '%s'",
                    type));
        }
        return result;
    }

    /**
     * Creates a new file rolling policy if the specified appender
     * configuration requires it.
     * 
     * @param context The Logback context
     * @param config The appender configuration
     * @return The created file rolling policy, or {@code null} if no policy is
     * required
     * @throws UnsupportedFileRollingPolicyException If no factory supports the
     * requested configuration type
     * @throws LogManagementException If an unexpected error occurs
     */
    public @Nullable LogbackFileRollingPolicy create(
            final LoggerContext context,
            final ManagedAppender config)
    throws LogManagementException {
        Objects.requireNonNull(context);
        Objects.requireNonNull(config);
        LogbackFileRollingPolicy result = null;
        final Optional<FileRollingPolicy> policy = config.getFileRollingPolicy();
        if (policy.isPresent()) {
            result = getFactory(policy.get().getClass()).create(context, config);
        }
        return result;
    }

    /**
     * Loads and returns the file rolling policy factories
     * registered through SPI for {@code LogbackFileRollingPolicyFactory}
     * interface.
     * 
     * @param config The log management configuration
     * @return The registered factories loaded through SPI
     */
    public static List<LogbackFileRollingPolicyFactory> loadSpiFactories(
            final LogManagementConfig config) {
        return ServiceLoader.load(LogbackFileRollingPolicyFactory.Provider.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .map(provider -> provider.create(config))
                .collect(Collectors.toList());
    }
}
