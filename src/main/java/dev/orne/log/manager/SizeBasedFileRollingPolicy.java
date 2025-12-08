package dev.orne.log.manager;

import java.util.Objects;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, size based log file rolling policy.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class SizeBasedFileRollingPolicy
extends FileRollingPolicy {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The maximum file size, in bytes. */
    private long fileSize;
    /** The maximum rolled file history count. */
    private Integer maxHistory;
    /** If the rolled files should be compressed. */
    private final boolean compressed;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected SizeBasedFileRollingPolicy(
            final BuilderImpl builder) {
        super(builder);
        this.fileSize = Objects.requireNonNull(builder.fileSize, "The file size must be set");
        this.maxHistory = builder.maxHistory;
        this.compressed = builder.compressed;
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
     * Returns the maximum rolled file history count.
     * 
     * @return The maximum rolled file history count
     */
    public Integer getMaxHistory() {
        return this.maxHistory;
    }

    /**
     * Returns if the rolled files should be compressed.
     * 
     * @return If the rolled files should be compressed
     */
    public boolean isCompressed() {
        return this.compressed;
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
                this.fileSize,
                this.maxHistory,
                this.compressed);
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
        final SizeBasedFileRollingPolicy other = (SizeBasedFileRollingPolicy) obj;
        return fileSize == other.fileSize
                && Objects.equals(maxHistory, other.maxHistory)
                && compressed == other.compressed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "SizeBasedFileRollingPolicy [code=%s, name=%s]",
                getCode(),
                getName());
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
         * Sets the maximum file size, in bytes.
         * 
         * @param size The maximum file size
         * @return This builder, for method chaining
         */
        Builder withFileSize(long size);

        /**
         * Sets the maximum rolled file history count.
         * 
         * @param maxHistory The maximum rolled file history count
         * @return This builder, for method chaining
         */
        Builder withMaxHistory(Integer maxHistory);

        /**
         * Sets if the rolled files should be compressed.
         * 
         * @param compressed If the rolled files should be compressed
         * @return This builder, for method chaining
         */
        Builder withCompressed(boolean compressed);

        /**
         * {@inheritDoc}
         */
        @Override
        SizeBasedFileRollingPolicy build();
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

        /** The maximum file size, in bytes. */
        private @Nullable Long fileSize;
        /** The maximum rolled file history count. */
        private @Nullable Integer maxHistory;
        /** If the rolled files should be compressed. */
        private boolean compressed;

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
            if (copy instanceof SizeBasedFileRollingPolicy) {
                final SizeBasedFileRollingPolicy tpCopy = (SizeBasedFileRollingPolicy) copy;
                this.fileSize = tpCopy.fileSize;
                this.maxHistory = tpCopy.maxHistory;
                this.compressed = tpCopy.compressed;
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
            this.maxHistory = maxHistory;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withCompressed(
                final boolean compressed) {
            this.compressed = compressed;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SizeBasedFileRollingPolicy build() {
            return new SizeBasedFileRollingPolicy(this);
        }
    }
}
