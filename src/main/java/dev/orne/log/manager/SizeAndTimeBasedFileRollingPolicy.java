package dev.orne.log.manager;

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
                "SizeAndTimeBasedFileRollingPolicy [code=%s, name=%s]",
                getCode(),
                getName());
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
        Builder withCode(String code);

        /**
         * {@inheritDoc}
         */
        @Override
        Builder withName(String name);

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
                final FileRollingPolicy copy) {
            super(copy);
            if (copy instanceof SizeAndTimeBasedFileRollingPolicy) {
                final SizeAndTimeBasedFileRollingPolicy tpCopy = (SizeAndTimeBasedFileRollingPolicy) copy;
                this.fileSize = tpCopy.fileSize;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withCode(
                final String code) {
            super.withCode(code);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withName(
                final String name) {
            super.withName(name);
            return this;
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
