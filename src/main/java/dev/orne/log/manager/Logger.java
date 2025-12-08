package dev.orne.log.manager;

import java.io.Serializable;
import java.util.Objects;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, logger.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class Logger
implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The name of the logger. */
    private final String name;
    /** The logging level of the logger. */
    private final Level level;
    /** If the logging level is inherited. */
    private final boolean levelInherited;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    public Logger(
            final BuilderImpl builder) {
        super();
        this.name = Objects.requireNonNull(builder.name, "The name must be set");
        this.level = Objects.requireNonNull(builder.level, "The level must be set");
        this.levelInherited = Objects.requireNonNull(builder.levelInherited, "The level inherited flag must be set");
    }

    /**
     * Creates an empty builder.
     * 
     * @return The created builder
     */
    public static Builder builder() {
        return new Logger.BuilderImpl();
    }

    /**
     * Creates a builder with an initial state copied from specified instance.
     * 
     * @param copy The instance to copy
     * @return The created builder
     */
    public static Builder copyOf(
            final Logger copy) {
        return new Logger.BuilderImpl(copy);
    }

    /**
     * Returns the name of the logger.
     * 
     * @return The name of the logger
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the effective logging level of the logger.
     * 
     * @return The effective logging level of the logger
     * @see #isLevelInherited()
     */
    public Level getLevel() {
        return this.level;
    }

    /**
     * Returns {@code true} if the logging level is inherited from the parent
     * logger.
     * Returns {@code false} if the logger has a explicit logging level set.
     * 
     * @return If the logging level is inherited
     */
    public boolean isLevelInherited() {
        return this.levelInherited;
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
                this.name,
                this.level,
                this.levelInherited);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Logger other = (Logger) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.level, other.level)
                && this.levelInherited == other.levelInherited;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Logger [name=%s, level=%s%s]",
                this.name,
                this.level,
                this.levelInherited ? " (inherited)" : "");
    }

    /**
     * Builder interface for {@link Logger} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    public interface Builder {

        /**
         * Sets the name of the logger.
         * 
         * @param name The name of the logger
         * @return This builder, for method chaining
         */
        Builder withName(String name);

        /**
         * Sets if the logging level is inherited.
         * 
         * @param inherited If the logging level is inherited
         * @return This builder, for method chaining
         */
        Builder withLevelInherited(boolean inherited);

        /**
         * Sets the logging level of the logger.
         * 
         * @param level The logging level of the logger
         * @return This builder, for method chaining
         */
        Builder withLevel(Level level);

        /**
         * Builds the logger instance.
         * 
         * @return The built logger instance
         */
        Logger build();
    }

    /**
     * Builder implementation for {@link Logger} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    static class BuilderImpl implements Builder {

        /** The name of the logger. */
        private @Nullable String name;
        /** If the logging level is inherited. */
        private @Nullable Boolean levelInherited;
        /** The logging level of the logger. */
        private @Nullable Level level;

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
                final Logger copy) {
            super();
            Objects.requireNonNull(copy, "Instance to copy cannot be null");
            this.name = copy.getName();
            this.level = copy.getLevel();
            this.levelInherited = copy.isLevelInherited();
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
        public BuilderImpl withLevelInherited(
                final boolean inherited) {
            this.levelInherited = inherited;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withLevel(
                final Level level) {
            this.level = level;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Logger build() {
            return new Logger(this);
        }
    }
}
