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

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, managed log appender.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class ManagedAppender
extends Appender {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The name of the destination file of the appender. */
    private String filename;
    /** The output format of the appender. */
    private String format;
    /** The output charset of the appender. */
    private String charset;
    /** The file rolling policy. */
    private @Nullable FileRollingPolicy fileRollingPolicy;

    /**
     * Creates a new instance.
     * 
     * @param builder The builder with the instance state
     */
    protected ManagedAppender(
            final BuilderImpl builder) {
        super(builder);
        this.filename = Objects.requireNonNull(builder.filename, "The output file name must be set");
        this.format = Objects.requireNonNull(builder.format, "The format must be set");
        this.charset = Objects.requireNonNull(builder.charset, "The charset must be set");
        this.fileRollingPolicy = builder.fileRollingPolicy;
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
     * Returns the name of the destination file of the appender.
     * 
     * @return The name of the destination file of the appender
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Returns the output format of the appender.
     * 
     * @return The output format of the appender
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Returns the output charset of the appender.
     * 
     * @return The output charset of the appender
     */
    public Charset getCharset() {
        return Charset.forName(this.charset);
    }

    /**
     * Returns the file rolling policy.
     * 
     * @return The file rolling policy
     */
    public Optional<FileRollingPolicy> getFileRollingPolicy() {
        return Optional.ofNullable(this.fileRollingPolicy);
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
                this.filename,
                this.format,
                this.charset,
                this.fileRollingPolicy);
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
        final ManagedAppender other = (ManagedAppender) obj;
        return Objects.equals(this.filename, other.filename)
                && Objects.equals(this.format, other.format)
                && Objects.equals(this.charset, other.charset)
                && Objects.equals(this.fileRollingPolicy, other.fileRollingPolicy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "ManagedAppenderImpl [name=%s]",
                this.getName());
    }

    /**
     * Builder interface for {@link ManagedAppender} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    public interface Builder
    extends Appender.Builder {

        /**
         * {@inheritDoc}
         */
        @Override
        Builder withName(String name);

        /**
         * Sets the name of the destination file of the appender.
         * 
         * @param filename The name of the destination file of the appender
         * @return This builder, for method chaining
         */
        Builder withFilename(String filename);

        /**
         * Sets the output format of the appender.
         * 
         * @param filename The output format of the appender
         * @return This builder, for method chaining
         */
        Builder withFormat(String format);

        /**
         * Sets the output charset of the appender.
         * 
         * @param charset The output charset of the appender
         * @return This builder, for method chaining
         */
        Builder withCharset(Charset charset);

        /**
         * Sets the file rolling policy.
         * 
         * @param policy The file rolling policy
         * @return This builder, for method chaining
         */
        Builder withFileRollingPolicy(@Nullable FileRollingPolicy policy);

        /**
         * {@inheritDoc}
         */
        @Override
        ManagedAppender build();
    }

    /**
     * Builder implementation for {@link ManagedAppender} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-12
     * @since 1.0
     */
    @API(status = API.Status.STABLE, since = "1.0.0")
    protected static class BuilderImpl
    extends Appender.BuilderImpl
    implements Builder {

        /** The name of the appender. */
        private @Nullable String name;
        /** The name of the destination file of the appender. */
        private @Nullable String filename;
        /** The output format of the appender. */
        private @Nullable String format;
        /** The output charset of the appender. */
        private @Nullable String charset;
        /** The file rolling policy. */
        private @Nullable FileRollingPolicy fileRollingPolicy;

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
                final ManagedAppender copy) {
            super(copy);
            this.filename = copy.getFilename();
            this.format = copy.getFormat();
            this.charset = copy.getCharset().name();
            this.fileRollingPolicy = copy.getFileRollingPolicy().orElse(null);
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
        public BuilderImpl withFilename(
                final String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withFormat(
                final String format) {
            this.format = format;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withCharset(
                final Charset charset) {
            this.charset = charset.name();
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BuilderImpl withFileRollingPolicy(
                final @Nullable FileRollingPolicy policy) {
            this.fileRollingPolicy = policy;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ManagedAppender build() {
            return new ManagedAppender(this);
        }
    }
}
