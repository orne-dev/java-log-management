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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import dev.orne.log.manager.InvalidAppenderConfigException;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.ManagedAppenderConfig;

/**
 * Factory for creating Logback managed appenders.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackAppenderFactory {

    /** The logger of the class. */
    public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LogbackAppenderFactory.class);

    /** The file rolling policy factories registry. */
    private final LogbackFileRollingPolicies policies;
    /** The base directory of log files. */
    private final Path baseDir;
    /** The pattern of valid file names. */
    private final Pattern filePattern;
    /** The extension of the output files. */
    private final String fileExtension;
    /** The fallback output format. */
    private final String fallbackFormat;
    /** The fallback output encoding. */
    private final Charset fallbackCharset;

    /**
     * Creates a new instance.
     * 
     * @param policies The file rolling policy factories registry.
     * @param config The log management configuration.
     */
    public LogbackAppenderFactory(
            final LogbackFileRollingPolicies policies,
            final LogManagementConfig config) {
        this.policies = Objects.requireNonNull(
                policies,
                "The Logback file rolling policy factories cannot be null");
        Objects.requireNonNull(
                config,
                "The configuration cannot be null");
        this.baseDir = config.getAppenderBaseDir();
        this.filePattern = config.getAppenderFilePattern();
        this.fileExtension = config.getAppenderFileExtension();
        this.fallbackFormat = config.getAppenderFallbackFormat();
        this.fallbackCharset = config.getAppenderFallbackCharset();
    }

    /** 
     * Returns the file rolling policy factories registry.
     * 
     * @return The file rolling policy factories registry
     */
    public LogbackFileRollingPolicies getPolicies() {
        return this.policies;
    }

    /** 
     * Returns the base directory of log files.
     * 
     * @return The base directory of log files
     */
    public Path getBaseDir() {
        return this.baseDir;
    }

    /** 
     * Returns the pattern of valid file names.
     * 
     * @return The pattern of valid file names
     */
    public Pattern getFilePattern() {
        return filePattern;
    }

    /** 
     * Returns the extension of the output files.
     * 
     * @return The extension of the output files
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /** 
     * Returns the fallback output format.
     * 
     * @return The fallback output format
     */
    public String getFallbackFormat() {
        return fallbackFormat;
    }

    /** 
     * Returns the fallback output encoding.
     * 
     * @return The fallback output encoding
     */
    public Charset getFallbackCharset() {
        return fallbackCharset;
    }

    /**
     * Creates a new Logback managed appender based on the specified
     * configuration.
     * 
     * @param context The Logback context
     * @param config The appender configuration
     * @return The created Logback managed appender
     * @throws LogManagementException If an error occurs creating the appender
     */
    public LogbackManagedAppender create(
            final LoggerContext context,
            final ManagedAppenderConfig config)
    throws LogManagementException {
        final ManagedAppender finalConfig = prepareConfig(config);
        final String file = getOutputFile(finalConfig.getFilename());
        final Encoder<ILoggingEvent> encoder = createEncoder(context, finalConfig);
        final LogbackFileRollingPolicy rolling = this.policies.create(context, finalConfig);
        final FileAppender<ILoggingEvent> result;
        if (rolling == null) {
            result = new FileAppender<>();
            result.setName(finalConfig.getName());
            result.setEncoder(encoder);
            result.setFile(file);
        } else {
            final RollingFileAppender<ILoggingEvent> creator = new RollingFileAppender<>();
            creator.setName(finalConfig.getName());
            creator.setEncoder(encoder);
            creator.setFile(file);
            creator.setRollingPolicy(rolling.getRollingPolicy());
            creator.setTriggeringPolicy(rolling.getTriggeringPolicy());
            result = creator;
        }
        result.setContext(context);
        result.setAppend(true);
        return new LogbackManagedAppender(finalConfig, result, encoder, rolling);
    }

    /**
     * Prepares the final appender configuration, verifying that the required
     * fields are set and/or setting default values.
     * 
     * @param config The original appender configuration
     * @return The final appender configuration
     */
    protected ManagedAppender prepareConfig(
            final ManagedAppenderConfig config) {
        final String name = config.getOptionalName()
                .orElseGet(() -> UUID.randomUUID().toString());
        return ManagedAppender
                .builder()
                .withName(name)
                .withFilename(config.getOptionalFilename().orElse(name))
                .withFormat(config.getOptionalFormat().orElse(this.fallbackFormat))
                .withCharset(config.getOptionalCharset().orElse(this.fallbackCharset))
                .withFileRollingPolicy(config.getOptionalFileRollingPolicy()
                        .orElse(null))
                .build();
    }

    /**
     * Returns the path of the output file.
     * 
     * @param file The filename of the appender configuration
     * @return The path of the output file
     * @throws InvalidAppenderConfigException If the filename of the appender
     * configuration is not valid
     */
    protected String getOutputFile(
            final String file) {
        if (!this.filePattern.matcher(file).matches()) {
            throw new InvalidAppenderConfigException(String.format(
                            "The file name '%s' is not valid", file));
        }
        return this.baseDir.resolve(file.concat(this.fileExtension))
                .toAbsolutePath()
                .toString();
    }

    /**
     * Creates the encoder to be used by the appender.
     * 
     * @param context The Logback context
     * @param config The appender configuration
     * @return The appender encoder
     */
    protected Encoder<ILoggingEvent> createEncoder(
            final LoggerContext context,
            final ManagedAppender config) {
        final PatternLayoutEncoder result = new PatternLayoutEncoder();
        result.setContext(context);
        result.setCharset(config.getCharset());
        result.setPattern(config.getFormat());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.policies,
                this.baseDir,
                this.filePattern.pattern(),
                this.fileExtension,
                this.fallbackFormat,
                this.fallbackCharset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LogbackAppenderFactory other = (LogbackAppenderFactory) obj;
        return Objects.equals(this.policies, other.policies)
                && Objects.equals(this.baseDir, other.baseDir)
                && Objects.equals(this.filePattern.pattern(), other.filePattern.pattern())
                && Objects.equals(this.fileExtension, other.fileExtension)
                && Objects.equals(this.fallbackFormat, other.fallbackFormat)
                && Objects.equals(this.fallbackCharset, other.fallbackCharset);
    }
}
