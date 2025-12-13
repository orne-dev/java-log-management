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

/**
 * Base, logging system agnostic, log file rolling policy configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public abstract class FileRollingPolicy
implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The maximum rolled file history count. */
    private final int maxHistory;
    /** If the rolled files should be compressed. */
    private final boolean compressed;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected FileRollingPolicy(
            final BuilderImpl builder) {
        super();
        this.maxHistory = builder.maxHistory;
        this.compressed = builder.compressed;
    }

    /**
     * Returns the maximum rolled file history count.
     * 
     * @return The maximum rolled file history count
     */
    public int getMaxHistory() {
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
     * Creates a builder with an initial state copied from this instance.
     * 
     * @return The created builder
     */
    public abstract Builder copy();

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FileRollingPolicy other = (FileRollingPolicy) obj;
        return Objects.equals(this.maxHistory, other.maxHistory)
                && this.compressed == other.compressed;
    }

    /**
     * Builder interface for {@link FileRollingPolicy} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    interface Builder {

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
    @API(status = API.Status.STABLE, since = "1.0.0")
    public abstract static class BuilderImpl
    implements Builder {

        /** The maximum rolled file history count. */
        private int maxHistory;
        /** If the rolled files should be compressed. */
        private boolean compressed;

        /**
         * Empty constructor.
         */
        protected BuilderImpl() {
            super();
        }

        /**
         * Copy constructor.
         * 
         * @param copy The instance to copy
         */
        protected BuilderImpl(
                final FileRollingPolicy copy) {
            this.maxHistory = copy.getMaxHistory();
            this.compressed = copy.isCompressed();
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
    }
}
