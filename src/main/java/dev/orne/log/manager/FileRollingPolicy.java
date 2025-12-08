package dev.orne.log.manager;

import java.io.Serializable;
import java.util.Objects;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, log file rolling policy.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class FileRollingPolicy
implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The code of the policy. */
    private final String code;
    /** The name of the policy. */
    private final String name;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected FileRollingPolicy(
            final BuilderImpl builder) {
        super();
        this.code = Objects.requireNonNull(builder.code, "The code must be set");
        this.name = Objects.requireNonNull(builder.name, "The name must be set");
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
     * Returns the code of the policy.
     * 
     * @return The code of the policy
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the name of the policy.
     * 
     * @return The name of the policy
     */
    public String getName() {
        return this.name;
    }

    /**
     * Creates a builder with an initial state copied from this instance.
     * 
     * @return The created builder
     */
    public Builder copy() {
        return new BuilderImpl(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.code,
                this.name);
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
        final FileRollingPolicy other = (FileRollingPolicy) obj;
        return Objects.equals(this.code, other.code)
                && Objects.equals(this.name, other.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "FileRollingPolicy [code=%s, name=%s]",
                this.code,
                this.name);
    }

    /**
     * Builder interface for {@link FileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    public interface Builder {

        /**
         * Sets the code of the policy.
         * 
         * @param code The code of the policy
         * @return This builder, for method chaining
         */
        Builder withCode(String code);

        /**
         * Sets the name of the policy.
         * 
         * @param name The name of the policy
         * @return This builder, for method chaining
         */
        Builder withName(String name);

        /**
         * Builds the file rolling policy instance.
         * 
         * @return The built file rolling policy instance
         */
        FileRollingPolicy build();
    }

    /**
     * Builder implementation for {@link FileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    protected static class BuilderImpl
    implements Builder {

        /** The code of the policy. */
        private @Nullable String code;
        /** The name of the policy. */
        private @Nullable String name;

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
            super();
            Objects.requireNonNull(copy, "Instance to copy cannot be null");
            this.code = copy.getCode();
            this.name = copy.getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withCode(
                final String code) {
            this.code = code;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withName(
                final String name) {
            this.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileRollingPolicy build() {
            return new FileRollingPolicy(this);
        }
    }
}
