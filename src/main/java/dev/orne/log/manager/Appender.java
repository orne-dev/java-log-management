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

import java.io.Serializable;
import java.util.Objects;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, log appender.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class Appender
implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The name of the appender. */
    private String name;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected Appender(
            final BuilderImpl builder) {
        super();
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
     * Returns the name of the appender.
     * 
     * @return The name of the appender
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
        return Objects.hash(this.name);
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
        final Appender other = (Appender) obj;
        return Objects.equals(this.name, other.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "Appender [name=%s]",
                this.name);
    }

    /**
     * Builder interface for {@link Appender} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    public interface Builder {

        /**
         * Sets the name of the level.
         * 
         * @param name The name of the level
         * @return This builder, for method chaining
         */
        Builder withName(String name);

        /**
         * Builds the appender instance.
         * 
         * @return The built appender instance
         */
        Appender build();
    }

    /**
     * Builder implementation for {@link Appender} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    protected static class BuilderImpl
    implements Builder {

        /** The name of the appender. */
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
                final Appender copy) {
            super();
            Objects.requireNonNull(copy, "Instance to copy cannot be null");
            this.name = copy.getName();
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
        public Appender build() {
            return new Appender(this);
        }
    }
}
