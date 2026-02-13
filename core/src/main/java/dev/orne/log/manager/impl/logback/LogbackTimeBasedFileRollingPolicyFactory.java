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
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.LogManagementConfig;
import dev.orne.log.manager.LogManagementException;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.log.manager.RollingPeriod;
import dev.orne.log.manager.TimeBasedFileRollingPolicy;

/**
 * Factory for time based Logback log file rolling policies.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class LogbackTimeBasedFileRollingPolicyFactory
implements LogbackFileRollingPolicyFactory {

    /** The rolled file name pattern. */
    public static final String FILENAME_PATTERN = "%s.%s.log";
    /** Fragment of filename pattern for hourly rolling period. */
    public static final String HOURLY_FILENAME_PATTERN = "%d{yyyy-MM-dd-HH}";
    /** Fragment of filename pattern for daily rolling period. */
    public static final String DAILY_FILENAME_PATTERN = "%d{yyyy-MM-dd}";
    /** Fragment of filename pattern for weekly rolling period. */
    public static final String WEEKLY_FILENAME_PATTERN = "%d{yyyy-ww}";
    /** Fragment of filename pattern for monthly rolling period. */
    public static final String MONTHLY_FILENAME_PATTERN = "%d{yyyy-MM}";

    /** The compressed rolled file suffix. */
    private final String compressedSuffix;

    /**
     * Empty constructor.
     */
    public LogbackTimeBasedFileRollingPolicyFactory(
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
        return TimeBasedFileRollingPolicy.class.equals(type);
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
        final TimeBasedFileRollingPolicy policyConfig = config.getFileRollingPolicy()
                .map(TimeBasedFileRollingPolicy.class::cast)
                .orElseThrow(() -> new LogManagementException(
                        "The appender configuration does not have a file rolling policy"));
        final String fileNamePattern = String.format(
                policyConfig.isCompressed()
                    ? FILENAME_PATTERN
                    : FILENAME_PATTERN + this.compressedSuffix,
                config.getFilename(),
                periodToPattern(policyConfig.getPeriod()));
        final TimeBasedRollingPolicy<ILoggingEvent> rolling = new TimeBasedRollingPolicy<>();
        rolling.setContext(context);
        rolling.setFileNamePattern(fileNamePattern);
        rolling.setMaxHistory(policyConfig.getMaxHistory());
        if (policyConfig.getMaxHistorySize().isPresent()) {
            rolling.setTotalSizeCap(new FileSize(policyConfig.getMaxHistorySize().getAsLong()));
        }
        return new LogbackTimeBasedFileRollingPolicy(
                policyConfig,
                rolling);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.compressedSuffix);
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
        final LogbackTimeBasedFileRollingPolicyFactory other = (LogbackTimeBasedFileRollingPolicyFactory) obj;
        return Objects.equals(this.compressedSuffix, other.compressedSuffix);
    }

    /**
     * Converts the library period to roll the files to Logback time based
     * filename pattern.
     * 
     * @param period The period to roll the files
     * @return The Logback time based filename pattern 
     */
    public static String periodToPattern(
            final RollingPeriod period) {
        final String result;
        switch (Objects.requireNonNull(period)) {
            case HOURLY:
                result = HOURLY_FILENAME_PATTERN;
                break;
            case DAILY:
                result = DAILY_FILENAME_PATTERN;
                break;
            case WEEKLY:
                result = WEEKLY_FILENAME_PATTERN;
                break;
            default: // MONTHLY
                result = MONTHLY_FILENAME_PATTERN;
                break;
        }
        return result;
    }

    /**
     * Provider for {@link LogbackTimeBasedFileRollingPolicyFactory}.
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
            return new LogbackTimeBasedFileRollingPolicyFactory(config);
        }
    }
}
