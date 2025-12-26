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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;

/**
 * Unit tests for {@link LogManagementConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class LogManagementConfigTest {

    /** Random generator. Accept insecure randomness for testing purposes. */
    private static final RandomUtils RND = RandomUtils.insecure();

    /**
     * Test utility classes well-formedness.
     */
    @Test
    void testUtilityClasses() {
        TestUtils.assertUtilityClass(LogManagementConfig.Properties.class);
        TestUtils.assertUtilityClass(LogManagementConfig.Defaults.class);
        TestUtils.assertUtilityClass(LogManagementConfig.SizeUnits.class);
    }

    /**
     * Test for {@link LogManagementConfig.SizeUnits#parse(String)}.
     */
    @Test
    void testSizeUnitParse() {
        assertThrows(
                IllegalArgumentException.class,
                () -> LogManagementConfig.SizeUnits.parse("10XYZ"));
        assertThrows(
                IllegalArgumentException.class,
                () -> LogManagementConfig.SizeUnits.parse("-10000"));
        assertEquals(
                LogManagementConfig.SizeUnits.UNBOUNDED_VALUE,
                LogManagementConfig.SizeUnits.parse(null));
        assertEquals(
                LogManagementConfig.SizeUnits.UNBOUNDED_VALUE,
                LogManagementConfig.SizeUnits.parse(
                    LogManagementConfig.SizeUnits.UNBOUNDED));
        assertEquals(
                10L,
                LogManagementConfig.SizeUnits.parse("10"));
        assertEquals(
                10L * 1024L,
                LogManagementConfig.SizeUnits.parse(
                    "10" + LogManagementConfig.SizeUnits.KB));
        assertEquals(
                10L * 1024L * 1024L,
                LogManagementConfig.SizeUnits.parse(
                    "10" + LogManagementConfig.SizeUnits.MB));
        assertEquals(
                10L * 1024L * 1024L * 1024L,
                LogManagementConfig.SizeUnits.parse(
                    "10" + LogManagementConfig.SizeUnits.GB));
        final long size = RND.randomLong(0, Long.MAX_VALUE);
        assertEquals(
                size,
                LogManagementConfig.SizeUnits.parse(String.valueOf(size)));
    }

    /**
     * Test for {@link LogManagementConfig.SizeUnits#format(long)}.
     */
    @Test
    void testSizeUnitFormat() {
        assertThrows(
                IllegalArgumentException.class,
                () -> LogManagementConfig.SizeUnits.format(-10000L));
        assertEquals(
                LogManagementConfig.SizeUnits.UNBOUNDED,
                LogManagementConfig.SizeUnits.format(0L));
        assertEquals(
                "512",
                LogManagementConfig.SizeUnits.format(512L));
        assertEquals(
                "1" + LogManagementConfig.SizeUnits.KB,
                LogManagementConfig.SizeUnits.format(1024L));
        assertEquals(
                "1" + LogManagementConfig.SizeUnits.MB,
                LogManagementConfig.SizeUnits.format(1024L * 1024L));
        assertEquals(
                "1" + LogManagementConfig.SizeUnits.GB,
                LogManagementConfig.SizeUnits.format(1024L * 1024L * 1024L));
        long size = RND.randomLong(0, Long.MAX_VALUE);
        if (size % 1024L == 0L) {
            size += RND.randomLong(1L, 1023L);
        }
        assertEquals(
                String.valueOf(size),
                LogManagementConfig.SizeUnits.format(size));
        
    }

    /**
     * Test default values.
     */
    @Test
    void testDefaultValues() {
        final Map<String, String> values = Map.of();
        final LogManagementConfig config = ((Config) values::get)
                .as(LogManagementConfig.class);
        assertEquals(
                Path.of(LogManagementConfig.Defaults.APPENDER_BASE_DIR),
                config.getAppenderBaseDir());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FILE_PATTERN,
                config.getAppenderFilePattern().pattern());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FILE_EXTENSION,
                config.getAppenderFileExtension());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FALLBACK_FORMAT,
                config.getAppenderFallbackFormat());
        assertEquals(
                LogManagementConfig.Defaults.APPENDER_FALLBACK_CHARSET,
                config.getAppenderFallbackCharset().name());
        assertEquals(
                LogManagementConfig.Defaults.ROLLING_PERIOD,
                config.getRollingByTimePeriod().name());
        assertEquals(
                LogManagementConfig.SizeUnits.parse(
                    LogManagementConfig.Defaults.ROLLING_MAX_SIZE),
                config.getRollingBySizeMaxSize());
        assertEquals(
                LogManagementConfig.Defaults.ROLLING_MAX_HISTORY,
                config.getRollingMaxHistory());
        assertEquals(
                LogManagementConfig.SizeUnits.parse(
                    LogManagementConfig.Defaults.ROLLING_MAX_HISTORY_SIZE),
                config.getRollingMaxHistorySize());
        assertEquals(
                LogManagementConfig.Defaults.ROLLING_COMPRESS,
                config.isRollingCompressionEnabled());
        assertEquals(
                LogManagementConfig.Defaults.ROLLING_COMPRESS_EXTENSION,
                config.getRollingCompressedExtension());
    }

    /**
     * Test configuration properties.
     */
    @Test
    void testConfigurationProperties() {
        final HashMap<String, String> values = new HashMap<>();
        values.put(LogManagementConfig.Properties.APPENDER_BASE_DIR, "/var/logs");
        values.put(LogManagementConfig.Properties.APPENDER_FILE_PATTERN, "app-\\w+");
        values.put(LogManagementConfig.Properties.APPENDER_FILE_EXTENSION, ".ext");
        values.put(LogManagementConfig.Properties.APPENDER_FALLBACK_FORMAT, "%m%n");
        values.put(LogManagementConfig.Properties.APPENDER_FALLBACK_CHARSET, "ISO-8859-1");
        values.put(LogManagementConfig.Properties.ROLLING_PERIOD, RollingPeriod.HOURLY.name());
        values.put(LogManagementConfig.Properties.ROLLING_MAX_SIZE, "100MB");
        values.put(LogManagementConfig.Properties.ROLLING_MAX_HISTORY, "24");
        values.put(LogManagementConfig.Properties.ROLLING_MAX_HISTORY_SIZE, "10GB");
        values.put(LogManagementConfig.Properties.ROLLING_COMPRESS, "false");
        values.put(LogManagementConfig.Properties.ROLLING_COMPRESS_EXTENSION, ".ext.gz");
        final LogManagementConfig config = ((Config) values::get)
                .as(LogManagementConfig.class);
        assertEquals(
                Path.of("/var/logs"),
                config.getAppenderBaseDir());
        assertEquals(
                "app-\\w+",
                config.getAppenderFilePattern().pattern());
        assertEquals(
                ".ext",
                config.getAppenderFileExtension());
        assertEquals(
                "%m%n",
                config.getAppenderFallbackFormat());
        assertEquals(
                "ISO-8859-1",
                config.getAppenderFallbackCharset().name());
        assertEquals(
                RollingPeriod.HOURLY,
                config.getRollingByTimePeriod());
        assertEquals(
                100L * 1024L * 1024L,
                config.getRollingBySizeMaxSize());
        assertEquals(
                24,
                config.getRollingMaxHistory());
        assertEquals(
                10L * 1024L * 1024L * 1024L,
                config.getRollingMaxHistorySize());
        assertFalse(
                config.isRollingCompressionEnabled());
        assertEquals(
                ".ext.gz",
                config.getRollingCompressedExtension());
    }
}
