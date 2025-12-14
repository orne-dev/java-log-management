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

import java.util.Objects;
import java.util.OptionalLong;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, time based log file rolling policy.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class TimeBasedFileRollingPolicy
extends FileRollingPolicy {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The period to roll the files. */
    private final RollingPeriod period;
    /** The maximum rolled file history size, in bytes. */
    private final @Nullable Long maxHistorySize;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected TimeBasedFileRollingPolicy(
            final BuilderImpl builder) {
        super(builder);
        this.period = Objects.requireNonNull(builder.period, "The period must be set");
        this.maxHistorySize = builder.maxHistorySize;
    }

    /**
     * Creates an empty builder.
     * 
     * @return The created builder
     */
    public static Builder builder() {
        return new BuilderImpl();
    }

    /**
     * Returns the period to roll the files.
     * 
     * @return The period to roll the files
     */
    public RollingPeriod getPeriod() {
        return this.period;
    }

    /**
     * Returns the maximum rolled file history size, in bytes.
     * 
     * @return The maximum rolled file history size
     */
    public OptionalLong getMaxHistorySize() {
        return this.maxHistorySize != null
                ? OptionalLong.of(this.maxHistorySize)
                : OptionalLong.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder copy() {
        return new BuilderImpl(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                this.period,
                this.maxHistorySize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        final TimeBasedFileRollingPolicy other = (TimeBasedFileRollingPolicy) obj;
        return this.period == other.period
                && Objects.equals(maxHistorySize, other.maxHistorySize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "TimeBasedFileRollingPolicy [period=%s, maxHistory=%s, maxHistorySize=%s, compressed=%s]",
                this.period,
                getMaxHistory(),
                this.maxHistorySize,
                isCompressed());
    }

    /**
     * Builder interface for {@link TimeBasedFileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    public interface Builder
    extends FileRollingPolicy.Builder {

        /**
         * Sets the period to roll the files.
         * 
         * @param period The period to roll the files
         * @return This builder, for method chaining
         */
        Builder withPeriod(RollingPeriod period);

        /**
         * {@inheritDoc}
         */
        @Override
        Builder withMaxHistory(Integer maxHistory);

        /**
         * Sets the maximum rolled file history size, in bytes.
         * 
         * @param maxHistorySize The maximum rolled file history size
         * @return This builder, for method chaining
         */
        Builder withMaxHistorySize(Long maxHistorySize);

        /**
         * {@inheritDoc}
         */
        @Override
        Builder withCompressed(boolean compressed);

        /**
         * {@inheritDoc}
         */
        @Override
        TimeBasedFileRollingPolicy build();
    }

    /**
     * Builder implementation for {@link TimeBasedFileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    protected static class BuilderImpl
    extends FileRollingPolicy.BuilderImpl
    implements Builder {

        /** The period to roll the files. */
        private @Nullable RollingPeriod period;
        /** The maximum rolled file history size, in bytes. */
        private @Nullable Long maxHistorySize;

        /**
         * Empty constructor.
         */
        public BuilderImpl() {
            super();
        }

        /**
         * Copy constructor.
         * 
         * @param copy The instance to copy
         */
        public BuilderImpl(
                final TimeBasedFileRollingPolicy copy) {
            super(copy);
            this.period = copy.getPeriod();
            this.maxHistorySize = copy.getMaxHistorySize().isPresent()
                    ? copy.getMaxHistorySize().getAsLong()
                    : null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withPeriod(
                final RollingPeriod period) {
            this.period = period;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withMaxHistory(
                final Integer maxHistory) {
            super.withMaxHistory(maxHistory);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withMaxHistorySize(
                final Long maxHistorySize) {
            this.maxHistorySize = maxHistorySize;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withCompressed(
                final boolean compressed) {
            super.withCompressed(compressed);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TimeBasedFileRollingPolicy build() {
            return new TimeBasedFileRollingPolicy(this);
        }
    }
}
