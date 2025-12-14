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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.apiguardian.api.API;

import dev.orne.config.Config;

/**
 * Configuration of the log management system.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public interface LogManagementConfig
extends Config {

    /**
     * Returns the base directory of managed appenders' log files.
     * 
     * @return The base directory of managed appenders' log files
     */
    default Path getAppenderBaseDir() {
        return Path.of(get(
                Properties.APPENDER_BASE_DIR,
                Defaults.APPENDER_BASE_DIR));
    }

    /**
     * Returns the pattern of managed appenders' valid log file names.
     * 
     * @return The pattern of managed appenders' valid log file names
     */
    default Pattern getAppenderFilePattern() {
        return Pattern.compile(get(
                Properties.APPENDER_FILE_PATTERN,
                Defaults.APPENDER_FILE_PATTERN));
    }

    /**
     * Returns the extension of managed appenders' log files.
     * 
     * @return The extension of managed appenders' log files
     */
    default String getAppenderFileExtension() {
        return get(
                Properties.APPENDER_FILE_EXTENSION,
                Defaults.APPENDER_FILE_EXTENSION);
    }

    /**
     * Returns the fallback format of managed appenders.
     * 
     * @return The fallback format of managed appenders
     */
    default String getAppenderFallbackFormat() {
        return get(
                Properties.APPENDER_FALLBACK_FORMAT,
                Defaults.APPENDER_FALLBACK_FORMAT);
    }

    /**
     * Returns the fallback encoding of managed appenders' log files.
     * 
     * @return The fallback encoding of managed appenders' log files
     */
    default Charset getAppenderFallbackCharset() {
        return Charset.forName(get(
                Properties.APPENDER_FALLBACK_CHARSET,
                Defaults.APPENDER_FALLBACK_CHARSET));
    }

    /**
     * Returns the period for time based log file rolling policies.
     * 
     * @return The period for time based log file rolling policies
     * @see RollingPeriod
     */
    default RollingPeriod getRollingByTimePeriod() {
        return RollingPeriod.valueOf(get(
                Properties.ROLLING_PERIOD,
                Defaults.ROLLING_PERIOD).toUpperCase());
    }

    /**
     * Returns the max size for size based log file rolling policies.
     * 
     * @return The max size for size based log file rolling policies
     */
    default long getRollingBySizeMaxSize() {
        return getLong(
                Properties.ROLLING_MAX_SIZE,
                Defaults.ROLLING_MAX_SIZE);
    }

    /**
     * Returns the max history for log file rolling policies.
     * 
     * @return The max history for log file rolling policies
     */
    default int getRollingMaxHistory() {
        return getInteger(
                Properties.ROLLING_MAX_HISTORY,
                Defaults.ROLLING_MAX_HISTORY);
    }

    /**
     * Returns the max history size for log file rolling policies.
     * 
     * @return The max history size for log file rolling policies
     */
    default long getRollingMaxHistorySize() {
        return getLong(
                Properties.ROLLING_MAX_HISTORY_SIZE,
                Defaults.ROLLING_MAX_HISTORY_SIZE);
    }

    /**
     * Returns whether compression is enabled for log file rolling policies.
     * 
     * @return {@code true} if compression is enabled; {@code false} otherwise
     */
    default boolean isRollingCompressionEnabled() {
        return getBoolean(
                Properties.ROLLING_COMPRESS,
                Defaults.ROLLING_COMPRESS);
    }

    /**
     * Returns the compressed rolled log file extension.
     * 
     * @return The compressed rolled log file extension
     */
    default String getRollingCompressedExtension() {
        return get(
                Properties.ROLLING_COMPRESS_EXTENSION,
                Defaults.ROLLING_COMPRESS_EXTENSION);
    }

    /**
     * Log management configuration properties.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    static final class Properties {

        /** The prefix of all log management configuration properties. */
        public static final String PREFIX =
                "log.management.";

        /** The base directory of managed appenders' log files. */
        public static final String APPENDER_BASE_DIR =
                PREFIX + "appender.dir";
        /** The pattern of managed appenders' valid log file names. */
        public static final String APPENDER_FILE_PATTERN =
                PREFIX + "appender.name.pattern";
        /** The extension of managed appenders' log files. */
        public static final String APPENDER_FILE_EXTENSION =
                PREFIX + "appender.extension";
        /**
         * The fallback format of managed appenders.
         * Valid formats depend on the underlying logging framework.
         */
        public static final String APPENDER_FALLBACK_FORMAT =
                PREFIX + "appender.format";
        /** The fallback encoding of managed appenders' log files. */
        public static final String APPENDER_FALLBACK_CHARSET =
                PREFIX + "appender.charset";

        /**
         * The name of the period constant for time based log file rolling policies.
         * 
         * @see RollingPeriod
         */
        public static final String ROLLING_PERIOD =
                PREFIX + "rolling.time.period";
        /** The max size for size based log file rolling policies. In bytes. */
        public static final String ROLLING_MAX_SIZE =
                PREFIX + "rolling.size.max";
        /** The log file rolling policies max history. */
        public static final String ROLLING_MAX_HISTORY =
                PREFIX + "rolling.history.max";
        /** The log file rolling policies max history size. */
        public static final String ROLLING_MAX_HISTORY_SIZE =
                PREFIX + "rolling.history.size.max";
        /** The log file rolling policies compression activation. */
        public static final String ROLLING_COMPRESS =
                PREFIX + "rolling.compress";
        /** The compressed rolled log file extension. */
        public static final String ROLLING_COMPRESS_EXTENSION =
                PREFIX + "rolling.compress.extension";

        /**
         * Private constructor to avoid instantiation.
         */
        private Properties() {
            throw new UnsupportedOperationException(
                    "This is a utility class and cannot be instantiated");
        }
    }

    /**
     * Log management configuration properties default values.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    static final class Defaults {

        /** The default base directory of managed appenders' log files. */
        public static final String APPENDER_BASE_DIR =
                System.getProperty("java.io.tmpdir");
        /** The default pattern of managed appenders' valid log file names. */
        public static final String APPENDER_FILE_PATTERN =
                "\\w+([\\.\\-]\\w+)*";
        /** The default extension of managed appenders' log files. */
        public static final String APPENDER_FILE_EXTENSION =
                ".log";
        /** The default fallback format of managed appenders. */
        public static final String APPENDER_FALLBACK_FORMAT =
                "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
        /** The default fallback encoding of new appenders' log files. */
        public static final String APPENDER_FALLBACK_CHARSET =
                "UTF-8";

        /** The default period for time based log file rolling policies. */
        public static final String ROLLING_PERIOD = RollingPeriod.DAILY.name();
        /**
         * The default max size for size based log file rolling policies.
         * 10 MB.
         */
        public static final long ROLLING_MAX_SIZE = 10L * 1024L * 1024L;
        /**
         * The default log file rolling policies max history.
         */
        public static final int ROLLING_MAX_HISTORY = 0;
        /**
         * The default log file rolling policies max history size.
         * Logback unbounded value.
         */
        public static final long ROLLING_MAX_HISTORY_SIZE = 0;
        /** The default log file rolling policies compression activation. */
        public static final boolean ROLLING_COMPRESS = true;
        /** The default compressed rolled log file extension. */
        public static final String ROLLING_COMPRESS_EXTENSION = ".zip";

        /**
         * Private constructor to avoid instantiation.
         */
        private Defaults() {
            throw new UnsupportedOperationException(
                    "This is a utility class and cannot be instantiated");
        }
    }
}
