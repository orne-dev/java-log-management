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

import java.util.Objects;

import org.apiguardian.api.API;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.SizeBasedFileRollingPolicy;

/**
 * Factory for size and time based Logback log file rolling policies.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackSizeBasedFileRollingPolicyFactory
implements LogbackFileRollingPolicyFactory {

    /** The minimum index of the builded policy. */
    public static final int MIN_INDEX = 1;
    /** The rolled file name pattern. */
    public static final String FILENAME_PATTERN = "%s.%%i.log";

    /** The compressed rolled file suffix. */
    private final String compressedSuffix;

    /**
     * Creates a new instance.
     * 
     * @param config The log management configuration
     */
    public LogbackSizeBasedFileRollingPolicyFactory(
            final LogManagementConfig config) {
        super();
        this.compressedSuffix = config.getRollingCompressedExtension();
    }

    /**
     * Returns the compressed rolled file suffix.
     * 
     * @return The compressed rolled file suffix
     */
    public String getCompressedSuffix() {
        return this.compressedSuffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(
            final Class<? extends FileRollingPolicy> type) {
        return SizeBasedFileRollingPolicy.class.equals(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogbackFileRollingPolicy create(
            final LoggerContext context,
            final ManagedAppender config)
    throws LogManagementException {
        Objects.requireNonNull(context);
        Objects.requireNonNull(config);
        final SizeBasedFileRollingPolicy policyConfig = config.getFileRollingPolicy()
                .map(SizeBasedFileRollingPolicy.class::cast)
                .orElseThrow(() -> new LogManagementException(
                        "The appender configuration does not have a file rolling policy"));
        final String fileNamePattern = String.format(
                policyConfig.isCompressed()
                        ? FILENAME_PATTERN
                        : FILENAME_PATTERN + this.compressedSuffix,
                config.getFilename());
        final FixedWindowRollingPolicy rolling =
                new FixedWindowRollingPolicy();
        rolling.setContext(context);
        rolling.setFileNamePattern(fileNamePattern);
        rolling.setMinIndex(MIN_INDEX);
        rolling.setMaxIndex(policyConfig.getMaxHistory());
        final SizeBasedTriggeringPolicy<ILoggingEvent> triggering =
                new SizeBasedTriggeringPolicy<>();
        triggering.setMaxFileSize(new FileSize(policyConfig.getFileSize()));
        triggering.setContext(context);
        return new LogbackSizeBasedFileRollingPolicy(
                policyConfig,
                rolling,
                triggering);
    }

    /**
     * Provider for {@link LogbackSizeBasedFileRollingPolicyFactory}.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.INTERNAL, since = "1.0.0")
    public static class Provider
    implements LogbackFileRollingPolicyFactory.Provider {

        /**
         * Creates a new instance.
         */
        public Provider() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LogbackFileRollingPolicyFactory create(
                final LogManagementConfig config) {
            return new LogbackSizeBasedFileRollingPolicyFactory(config);
        }
    }
}
