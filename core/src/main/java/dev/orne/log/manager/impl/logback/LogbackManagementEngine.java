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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.LoggerNameUtil;
import dev.orne.log.manager.Appender;
import dev.orne.log.manager.AppenderNotFoundException;
import dev.orne.log.manager.IllegalLevelException;
import dev.orne.log.manager.Level;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.LogManagementEngine;
import dev.orne.log.manager.Logger;
import dev.orne.log.manager.LoggerNotFoundException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.ManagedAppenderConfig;
import dev.orne.log.manager.UnmanagedAppenderException;

/**
 * Logback based implementation of {@code LogManagementEngine}.
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
@API(status = API.Status.STABLE, since = "1.0.0")
public class LogbackManagementEngine
implements LogManagementEngine {

    /** The class logger. */
    private static final org.slf4j.Logger LOG =
            LoggerFactory.getLogger(LogbackManagementEngine.class);

    /** The root logger name. */
    private static final String ROOT_LOGGER_NAME = org.slf4j.Logger.ROOT_LOGGER_NAME;
    /** The supported log levels. */
    private static final List<Level> LEVELS = Stream.of(
                ch.qos.logback.classic.Level.TRACE,
                ch.qos.logback.classic.Level.DEBUG,
                ch.qos.logback.classic.Level.INFO,
                ch.qos.logback.classic.Level.WARN,
                ch.qos.logback.classic.Level.ERROR,
                ch.qos.logback.classic.Level.OFF)
            .map(level -> Level.builder()
                    .withName(level.levelStr)
                    .withValue(level.toInt())
                    .build())
            .collect(Collectors.toList());
    /** The level name to level map. */
    private static final Map<String, Level> LEVEL_MAP = LEVELS.stream()
            .collect(Collectors.toMap(
                    Level::getName,
                    level -> level));
    /** The logger not found message template. */
    private static final String LOGGER_NOT_FOUND = "Logger not found: %s";

    /** The Logback context cache. */
    private static SoftReference<LoggerContext> contextRef;
    /** The managed Logback appenders factory. */
    private final LogbackAppenderFactory appenderFactory;
    /** The registry of known appenders. */
    private @Nullable Map<String, LogbackAppender> appendersRegistry;

    /**
     * Creates a new instance.
     * Loads available file rolling policies from SPI.
     * 
     * @param config The log management configuration
     */
    public LogbackManagementEngine(
            final LogManagementConfig config) {
        super();
        this.appenderFactory = new LogbackAppenderFactory(
                new LogbackFileRollingPolicies(config),
                config);
    }

    /**
     * Creates a new instance.
     * 
     * @param policies The available file rolling policies
     * @param config The log management configuration
     */
    public LogbackManagementEngine(
            final LogbackFileRollingPolicies policies,
            final LogManagementConfig config) {
        super();
        this.appenderFactory = new LogbackAppenderFactory(policies, config);
        this.appendersRegistry = new HashMap<>();
    }

    /**
     * Returns the managed Logback appenders factory.
     * 
     * @return The managed Logback appenders factory
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected LogbackAppenderFactory getAppenderFactory() {
        return this.appenderFactory;
    }

    /**
     * Returns the registry of known appenders.
     * <p>
     * On first call will scan for appenders created through Logback
     * configuration.
     * 
     * @return The registry of known appenders
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected synchronized Map<String, LogbackAppender> getAppendersRegistry() {
        if (this.appendersRegistry == null) {
            this.appendersRegistry = scanAppenders();
        }
        return this.appendersRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Level> getLevels()
    throws LogManagementException {
        return List.copyOf(LEVELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getRoot()
    throws LogManagementException {
        return getLogger(ROOT_LOGGER_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Logger> findLogger(
            final String name)
    throws LogManagementException {
        return Optional.ofNullable(getContext().exists(name))
                .map(this::createLogger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger(
            final String name)
    throws LogManagementException {
        return createLogger(getContext().getLogger(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Logger> getChildren(
            final String parent)
    throws LogManagementException {
        final List<String> parentParts;
        if (ROOT_LOGGER_NAME.equals(parent)) {
            parentParts = List.of();
        } else {
            parentParts = LoggerNameUtil.computeNameParts(parent);
        }
        final Map<String, Logger> result = new LinkedHashMap<>();
        for (final ch.qos.logback.classic.Logger logger : getContext().getLoggerList()) {
            final List<String> parts = LoggerNameUtil.computeNameParts(logger.getName());
            if (parts.size() == parentParts.size() + 1) {
                final String finalPart = parts.remove(parts.size() - 1);
                if (parentParts.equals(parts) &&
                        !ROOT_LOGGER_NAME.equals(logger.getName())) {
                    result.put(finalPart, createLogger(logger));
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger setLevel(
            final String logger,
            final @Nullable String level)
    throws LogManagementException {
        final ch.qos.logback.classic.Logger natLogger = getContext().exists(logger);
        if (natLogger == null) {
            throw new LoggerNotFoundException(String.format(
                    LOGGER_NOT_FOUND, logger));
        }
        if (level == null) {
            if (ROOT_LOGGER_NAME.equals(logger)) {
                throw new IllegalLevelException(
                        "Cannot unset level of root logger");
            }
            natLogger.setLevel(null);
            LOG.info("Logger '{}' level unset", logger);
        } else {
            final ch.qos.logback.classic.Level natLevel =
                    ch.qos.logback.classic.Level.toLevel(level, null);
            if (natLevel == null) {
                throw new IllegalLevelException(
                        "Unknown level: " + logger);
            }
            natLogger.setLevel(natLevel);
            LOG.info("Logger '{}' level set to {}", logger, level);
        }
        return createLogger(natLogger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appender> getAppenders()
    throws LogManagementException {
        return getAppendersRegistry().values()
                .stream()
                .map(LogbackAppender::getData)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAppenders(
            final String logger)
    throws LogManagementException {
        final ch.qos.logback.classic.Logger natLogger =
                getContext().exists(logger);
        if (natLogger == null) {
            throw new LoggerNotFoundException(
                    "Logger not found: " + logger);
        }
        final Iterable<ch.qos.logback.core.Appender<ILoggingEvent>> appenders =
                natLogger::iteratorForAppenders;
        return StreamSupport.stream(appenders.spliterator(), false)
                .map(ch.qos.logback.core.Appender::getName)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Appender getAppender(
            final String name)
    throws LogManagementException {
        return getLogbackAppender(name).getData();
    }

    /**
     * Returns the {@code LogbackAppender} with the specified name.
     * <p>
     * If no appender exist for the given name a
     * {@code AppenderNotFoundException} is thrown.
     * 
     * @param name The name of the appender
     * @return The existing Logback appender
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected LogbackAppender getLogbackAppender(
            final String name) {
        final LogbackAppender result = getAppendersRegistry().get(name);
        if (result == null) {
            throw new AppenderNotFoundException(
                    "Appender not found: " + name);
        }
        return result;
    }

    /**
     * Returns the {@code LogbackManagedAppender} with the specified name created by
     * this instance.
     * <p>
     * If no appender exist for the given name a
     * {@code AppenderNotFoundException} is thrown.
     * <p>
     * If appender was created through Logback configuration a
     * {@code UnmanagedAppenderException} is thrown.
     * 
     * @param name The name of the appender
     * @return The existing Logback appender
     * @throws UnmanagedAppenderException If the appender is not managed
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected LogbackManagedAppender getManagedAppender(
            final String name) {
        final LogbackAppender appender = getLogbackAppender(name);
        if (!(appender instanceof LogbackManagedAppender)) {
            throw new UnmanagedAppenderException(String.format(
                    "The operation cannot be performed with appender '%s'. The appender is not managed.", name));
        }
        return (LogbackManagedAppender) appender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagedAppender createAppender(
            final ManagedAppenderConfig config)
    throws LogManagementException {
        final LogbackManagedAppender appender = this.appenderFactory.create(getContext(), config);
        final ManagedAppender result = appender.getData();
        appender.start();
        getAppendersRegistry().put(result.getName(), appender);
        LOG.info("Appender '{}' created", result.getName());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteAppender(
            final String appender)
    throws LogManagementException {
        boolean removed = false;
        final LogbackManagedAppender appenderImpl = getManagedAppender(appender);
        for (final ch.qos.logback.classic.Logger logger : getContext().getLoggerList()) {
            final boolean removedFromLogger = logger.detachAppender(appenderImpl.getAppender());
            if (removedFromLogger) {
                LOG.info("Appender '{}' detached from '{}'", appender, logger.getName());
            }
            removed = removed || removedFromLogger;
        }
        appenderImpl.stop();
        getAppendersRegistry().remove(appender);
        LOG.info("Appender '{}' destroyed", appender);
        return removed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        final ch.qos.logback.classic.Logger natLogger = getContext().exists(logger);
        if (natLogger == null) {
            throw new LoggerNotFoundException(String.format(
                    LOGGER_NOT_FOUND, logger));
        }
        final LogbackManagedAppender appenderImpl = getManagedAppender(appender);
        natLogger.addAppender(appenderImpl.getAppender());
        LOG.info("Appender '{}' attached to '{}'", appender, logger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean detachAppender(
            final String logger,
            final String appender)
    throws LogManagementException {
        final ch.qos.logback.classic.Logger natLogger = getContext().exists(logger);
        if (natLogger == null) {
            throw new LoggerNotFoundException(String.format(
                    LOGGER_NOT_FOUND, logger));
        }
        final LogbackManagedAppender appenderImpl = getManagedAppender(appender);
        final boolean result = natLogger.detachAppender(appenderImpl.getAppender());
        if (result) {
            LOG.info("Appender '{}' detached from '{}'", appender, logger);
        } else {
            LOG.info("Appender '{}' was not attached to '{}'", appender, logger);
        }
        return result;
    }

    /**
     * Returns the Logback context.
     * 
     * @return The Logback context
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected static synchronized LoggerContext getContext() {
        LoggerContext context = null;
        if (contextRef != null) {
            context = contextRef.get();
        }
        if (context == null) {
            context = (LoggerContext) LoggerFactory.getILoggerFactory();
            contextRef = new SoftReference<>(context);
        }
        return context;
    }

    /**
     * Creates a {@code Logger} instance from the given Logback logger.
     * 
     * @param logger The Logback logger
     * @return The created {@code Logger} instance
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected Logger createLogger(
            final ch.qos.logback.classic.Logger logger) {
        return Logger.builder()
                .withName(logger.getName())
                .withLevel(LEVEL_MAP.get(logger.getEffectiveLevel().toString()))
                .withLevelInherited(logger.getLevel() != null)
                .build();
    }

    /**
     * Scans the existing Logback logger for attached appenders.
     * 
     * @return A map with the attached appenders, by name
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    protected Map<String, LogbackAppender> scanAppenders() {
        final LoggerContext context = getContext();
        final Map<String, LogbackAppender> result = new HashMap<>();
        for (final ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            final Iterator<ch.qos.logback.core.Appender<ILoggingEvent>> it = logger.iteratorForAppenders();
            while (it.hasNext()) {
                final ch.qos.logback.core.Appender<?> appender = it.next();
                result.computeIfAbsent(appender.getName(), key -> new LogbackUnmanagedAppender(
                        Appender.builder()
                            .withName(appender.getName())
                            .build()));
            }
        }
        return result;
    }
}
