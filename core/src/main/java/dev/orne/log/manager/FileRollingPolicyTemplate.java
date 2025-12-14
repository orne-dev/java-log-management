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
 * Base, logging system agnostic, log file rolling policy template.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class FileRollingPolicyTemplate
implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The code of the policy. */
    private final String code;
    /** The name of the policy. */
    private final String name;
    /** The configuration of the policy. */
    private final FileRollingPolicy policy;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected FileRollingPolicyTemplate(
            final BuilderImpl builder) {
        super();
        this.code = Objects.requireNonNull(builder.code, "The code must be set");
        this.name = Objects.requireNonNull(builder.name, "The name must be set");
        this.policy = Objects.requireNonNull(builder.policy, "The policy must be set");
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
            final FileRollingPolicyTemplate copy) {
        return new BuilderImpl(copy);
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
     * Returns the configuration of the policy.
     * 
     * @return The configuration of the policy
     */
    public FileRollingPolicy getPolicy() {
        return this.policy;
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
     * Creates a new builder to create a copy of the policy configuration.
     * 
     * @return The created builder
     */
    public FileRollingPolicy.Builder newPolicy() {
        return this.policy.copy();
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
        final FileRollingPolicyTemplate other = (FileRollingPolicyTemplate) obj;
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
     * Builder interface for {@link FileRollingPolicyTemplate} instances.
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
         * Sets the configuration of the policy.
         * 
         * @param policy The configuration of the policy
         * @return This builder, for method chaining
         */
        Builder withPolicy(FileRollingPolicy policy);

        /**
         * Builds the file rolling policy instance.
         * 
         * @return The built file rolling policy instance
         */
        FileRollingPolicyTemplate build();
    }

    /**
     * Builder implementation for {@link FileRollingPolicyTemplate} instances.
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
        /** The configuration of the policy. */
        private @Nullable FileRollingPolicy policy;

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
                final FileRollingPolicyTemplate copy) {
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
        public Builder withPolicy(
                final FileRollingPolicy policy) {
            this.policy = policy;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileRollingPolicyTemplate build() {
            return new FileRollingPolicyTemplate(this);
        }
    }
}
