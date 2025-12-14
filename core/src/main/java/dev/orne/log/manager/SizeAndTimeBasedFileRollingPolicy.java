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

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, size and time based log file rolling policy.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class SizeAndTimeBasedFileRollingPolicy
extends TimeBasedFileRollingPolicy {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The maximum file size, in bytes. */
    private long fileSize;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected SizeAndTimeBasedFileRollingPolicy(
            final BuilderImpl builder) {
        super(builder);
        this.fileSize = Objects.requireNonNull(builder.fileSize, "The file size must be set");
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
     * Creates a builder with an initial state copied from the specified
     * instance.
     * 
     * @param copy The instance to copy
     * @return The created builder
     */
    public static Builder builder(
            final SizeAndTimeBasedFileRollingPolicy copy) {
        return new BuilderImpl(copy);
    }

    /**
     * Returns the maximum file size, in bytes.
     * 
     * @return The maximum file size
     */
    public long getFileSize() {
        return this.fileSize;
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
                this.fileSize);
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SizeAndTimeBasedFileRollingPolicy other = (SizeAndTimeBasedFileRollingPolicy) obj;
        return fileSize == other.fileSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "SizeAndTimeBasedFileRollingPolicy [period=%s, fileSize=%s, maxHistory=%s, maxHistorySize=%s, compressed=%s]",
                getPeriod(),
                this.fileSize,
                getMaxHistory(),
                getMaxHistorySize(),
                isCompressed());
    }

    /**
     * Builder interface for {@link SizeAndTimeBasedFileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    public interface Builder
    extends TimeBasedFileRollingPolicy.Builder {

        /**
         * {@inheritDoc}
         */
        @Override
        Builder withPeriod(RollingPeriod period);

        /**
         * Sets the maximum file size, in bytes.
         * 
         * @param size The maximum file size
         * @return This builder, for method chaining
         */
        Builder withFileSize(long size);

        /**
         * {@inheritDoc}
         */
        @Override
        Builder withMaxHistory(Integer maxHistory);

        /**
         * {@inheritDoc}
         */
        @Override
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
        SizeAndTimeBasedFileRollingPolicy build();
    }

    /**
     * Builder implementation for {@link SizeAndTimeBasedFileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    protected static class BuilderImpl
    extends TimeBasedFileRollingPolicy.BuilderImpl
    implements Builder {

        /** The maximum file size, in bytes. */
        private @Nullable Long fileSize;

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
                final SizeAndTimeBasedFileRollingPolicy copy) {
            super(copy);
            this.fileSize = copy.getFileSize();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withPeriod(
                final RollingPeriod period) {
            super.withPeriod(period);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withFileSize(
                final long fileSize) {
            this.fileSize = fileSize;
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
            super.withMaxHistorySize(maxHistorySize);
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
        public SizeAndTimeBasedFileRollingPolicy build() {
            return new SizeAndTimeBasedFileRollingPolicy(this);
        }
    }
}
