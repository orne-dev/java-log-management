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

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.RollingPolicyBase;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.rolling.helper.CompressionMode;

/**
 * Logback status snapshot holder.
 * <p>
 * Used to capture and hold the status of Logback at a certain point in time.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
class LogbackStatusSnapshot {

    /** The logger context. */
    private final LoggerContext context;
    /** The loggers status. */
    private final Map<String, LoggerSnapshot> loggers;
    /** The appenders status. */
    private final Map<String, AppenderSnapshot> appenders;

    /**
     * Creates a new instance.
     * 
     * @param context The logger context
     */
    public LogbackStatusSnapshot(
            final LoggerContext context) {
        super();
        this.context = context;
        HashMap<String, LoggerSnapshot> loggersTmp = new HashMap<>();
        HashMap<String, AppenderSnapshot> appendersTmp = new HashMap<>();
        for (final Logger logger : this.context.getLoggerList()) {
            loggersTmp.put(logger.getName(), LoggerSnapshot.of(logger, appendersTmp));
        }
        this.loggers = Map.copyOf(loggersTmp);
        this.appenders = Map.copyOf(appendersTmp);
    }

    /**
     * Captures the current Logback status from the specified context.
     * 
     * @param context The logger context
     * @return The captured snapshot
     */
    public static LogbackStatusSnapshot capture(
            final LoggerContext context) {
        return new LogbackStatusSnapshot(context);
    }

    /**
     * Asserts that this snapshot has the same status as the specified one.
     * 
     * @param other The other snapshot
     */
    public void assertSameStatus(
            final LogbackStatusSnapshot other) {
        assertSame(this.context, other.context, "Logback contexts differ");
        for (final LoggerSnapshot logger : this.loggers.values()) {
            if (!logger.equals(other.loggers.get(logger.name))) {
                throw new AssertionError("Logger status differ");
            }
        }
        for (final AppenderSnapshot appender : this.appenders.values()) {
            if (!appender.equals(other.appenders.get(appender.name))) {
                throw new AssertionError("Appender configuration differ");
            }
        }
    }

    /**
     * Logger status snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class LoggerSnapshot {

        /** The logger name. */
        private final String name;
        /** The logger level. */
        private final @Nullable String level;
        /** The attached appenders. */
        private final List<String> appenders;

        /**
         * Creates a new instance.
         * 
         * @param name The logger name
         * @param level The logger level
         * @param appenders The attached appenders
         */
        private LoggerSnapshot(
                final String name,
                final @Nullable String level,
                final List<String> appenders) {
            super();
            this.name = name;
            this.level = level;
            this.appenders = appenders;
        }

        /**
         * Creates a snapshot from the specified logger.
         * 
         * @param logger The logger
         * @return The logger snapshot
         */
        public static LoggerSnapshot of(
                final Logger logger,
                final Map<String, AppenderSnapshot> appendersRegistry) {
            final Iterable<Appender<ILoggingEvent>> appenders =
                    logger::iteratorForAppenders;
            final List<String> appenderNames = StreamSupport.stream(appenders.spliterator(), false)
                    .map(appender -> {
                        appendersRegistry.computeIfAbsent(
                                appender.getName(),
                                k -> AppenderSnapshot.of(appender));
                        return appender.getName();
                    })
                    .collect(Collectors.toList());
            return new LoggerSnapshot(
                    logger.getName(),
                    logger.getLevel() != null ? logger.getLevel().toString() : null,
                    List.copyOf(appenderNames));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    this.name,
                    this.level,
                    this.appenders);
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
            final LoggerSnapshot other = (LoggerSnapshot) obj;
            return Objects.equals(this.name, other.name)
                    && Objects.equals(this.level, other.level)
                    && Objects.equals(this.appenders, other.appenders);
        }
    }

    /**
     * Appender status snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class AppenderSnapshot {

        /** The appender name. */
        private final String name;
        /** The appender type. */
        private final String type;
        /** The encoder configuration. */
        private final EncoderSnapshot encoder;
        /** The file rolling triggering policy configuration. */
        private final TriggeringPolicySnapshot triggeringPolicy;
        /** The file rolling policy configuration. */
        private final RollingPolicySnapshot rollingPolicy;

        public AppenderSnapshot(
                final Appender<?> appender) {
            super();
            this.name = appender.getName();
            this.type = appender.getClass().getName();
            if (appender instanceof OutputStreamAppender) {
                this.encoder = EncoderSnapshot.of(
                        ((OutputStreamAppender<?>) appender).getEncoder());
                if (appender instanceof RollingFileAppender) {
                    final RollingFileAppender<?> rfa =
                            (RollingFileAppender<?>) appender;
                    this.triggeringPolicy = TriggeringPolicySnapshot.of(rfa.getTriggeringPolicy());
                    this.rollingPolicy = RollingPolicySnapshot.of(rfa.getRollingPolicy());
                } else {
                    this.triggeringPolicy = null;
                    this.rollingPolicy = null;
                }
            } else {
                this.encoder = null;
                this.triggeringPolicy = null;
                this.rollingPolicy = null;
            }
        }

        public static AppenderSnapshot of(
                final Appender<ILoggingEvent> appender) {
            return new AppenderSnapshot(appender);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    this.name,
                    this.type,
                    this.encoder,
                    this.triggeringPolicy,
                    this.rollingPolicy);
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
            final AppenderSnapshot other = (AppenderSnapshot) obj;
            return Objects.equals(this.name, other.name)
                    && Objects.equals(this.type, other.type)
                    && Objects.equals(this.encoder, other.encoder)
                    && Objects.equals(this.rollingPolicy, other.rollingPolicy)
                    && Objects.equals(this.triggeringPolicy, other.triggeringPolicy);
        }
    }

    /**
     * Encoder configuration snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class EncoderSnapshot {

        /** The encoder type. */
        private final String type;
        /** The pattern. */
        private final String pattern;

        /**
         * Creates a new instance.
         * 
         * @param encoder The encoder
         */
        public EncoderSnapshot(
                final Encoder<?> encoder) {
            super();
            this.type = encoder.getClass().getName();
            if (encoder instanceof PatternLayoutEncoder) {
                this.pattern = ((PatternLayoutEncoder) encoder).getPattern();
            } else {
                this.pattern = null;
            }
        }

        /**
         * Creates a snapshot from the specified logger.
         * 
         * @param logger The logger
         * @return The logger snapshot
         */
        public static EncoderSnapshot of(
                final Encoder<?> encoder) {
            return new EncoderSnapshot(encoder);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    this.type,
                    this.pattern);
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
            final EncoderSnapshot other = (EncoderSnapshot) obj;
            return Objects.equals(this.type, other.type)
                    && Objects.equals(this.pattern, other.pattern);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    /**
     * File rolling triggering policy configuration snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class TriggeringPolicySnapshot {

        /** The triggering policy type. */
        private final String type;
        /** The maximum file size. */
        private final String maxFileSize;

        /**
         * Creates a new instance.
         * 
         * @param policy The triggering policy
         */
        public TriggeringPolicySnapshot(
                final TriggeringPolicy<?> policy) {
            super();
            this.type = policy.getClass().getName();
            if (policy instanceof SizeBasedTriggeringPolicy) {
                this.maxFileSize = ((SizeBasedTriggeringPolicy<?>) policy).getMaxFileSize().toString();
            } else {
                this.maxFileSize = null;
            }
        }

        /**
         * Creates a snapshot from the specified logger.
         * 
         * @param policy The triggering policy
         * @return The triggering policy configuration snapshot
         */
        public static TriggeringPolicySnapshot of(
                final TriggeringPolicy<?> policy) {
            return new TriggeringPolicySnapshot(policy);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    this.type,
                    this.maxFileSize);
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
            final TriggeringPolicySnapshot other = (TriggeringPolicySnapshot) obj;
            return Objects.equals(this.type, other.type)
                    && Objects.equals(this.maxFileSize, other.maxFileSize);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    /**
     * Rolling policy snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class RollingPolicySnapshot {

        /** The rolling policy type. */
        private final String type;
        /** The file name pattern. */
        private final String fileNamePattern;
        /** The compression mode. */
        private final CompressionMode compressionMode;

        public RollingPolicySnapshot(
                final RollingPolicy policy) {
            super();
            this.type = policy.getClass().getName();
            this.compressionMode = policy.getCompressionMode();
            if (policy instanceof RollingPolicyBase) {
                this.fileNamePattern = ((RollingPolicyBase) policy).getFileNamePattern();
            } else {
                this.fileNamePattern = null;
            }
        }

        /**
         * Creates a snapshot from the specified logger.
         * 
         * @param policy The rolling policy
         * @return The rolling policy configuration snapshot
         */
        public static RollingPolicySnapshot of(
                final RollingPolicy policy) {
            if (policy instanceof FixedWindowRollingPolicy) {
                return new FixedRollingPolicySnapshot(
                        (FixedWindowRollingPolicy) policy);
            } else {
                return new RollingPolicySnapshot(policy);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    this.type,
                    this.fileNamePattern,
                    this.compressionMode);
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
            final RollingPolicySnapshot other = (RollingPolicySnapshot) obj;
            return Objects.equals(this.type, other.type)
                    && Objects.equals(this.fileNamePattern, other.fileNamePattern)
                    && this.compressionMode == other.compressionMode;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    /**
     * Fixed window rolling policy configuration snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class FixedRollingPolicySnapshot
    extends RollingPolicySnapshot{

        /** The minimum index. */
        private final int minIndex;
        /** The maximum index. */
        private final int maxIndex;

        /**
         * Creates a new instance.
         * 
         * @param policy The fixed window rolling policy
         */
        public FixedRollingPolicySnapshot(
                final FixedWindowRollingPolicy policy) {
            super(policy);
            this.minIndex = policy.getMinIndex();
            this.maxIndex = policy.getMaxIndex();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    super.hashCode(),
                    this.minIndex,
                    this.maxIndex);
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
            if (!super.equals(obj)) {
                return false;
            }
            final FixedRollingPolicySnapshot other = (FixedRollingPolicySnapshot) obj;
            return this.minIndex == other.minIndex
                    && this.maxIndex == other.maxIndex;
        }
    }

    /**
     * Time based rolling policy configuration snapshot holder.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    static class TimeBasedRollingPolicySnapshot
    extends RollingPolicySnapshot{

        /** The max history. */
        private final int maxHistory;

        /**
         * Creates a new instance.
         * 
         * @param policy The time based rolling policy
         */
        public TimeBasedRollingPolicySnapshot(
                final TimeBasedRollingPolicy<?> policy) {
            super(policy);
            this.maxHistory = policy.getMaxHistory();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(
                    super.hashCode(),
                    this.maxHistory);
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
            if (!super.equals(obj)) {
                return false;
            }
            final TimeBasedRollingPolicySnapshot other = (TimeBasedRollingPolicySnapshot) obj;
            return this.maxHistory == other.maxHistory;
        }
    }
}
