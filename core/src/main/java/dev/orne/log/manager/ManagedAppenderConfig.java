package dev.orne.log.manager;

import java.io.Serializable;
import java.nio.charset.Charset;

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
import java.util.Optional;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * Base, logging system agnostic, new managed log appender configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0.0")
public class ManagedAppenderConfig
implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

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
     * Creates a new instance.
     * 
     * @param copy The instance to copy
     */
    public ManagedAppenderConfig() {
        super();
    }

    /**
     * Creates a new instance.
     * 
     * @param copy The instance to copy
     */
    public ManagedAppenderConfig(
            final ManagedAppenderConfig copy) {
        super();
        this.name = copy.name;
        this.filename = copy.filename;
        this.format = copy.format;
        this.charset = copy.charset;
        this.fileRollingPolicy = copy.fileRollingPolicy;
    }

    /**
     * Returns the name of the appender.
     * 
     * @return The name of the appender
     */
    public @Nullable String getName() {
        return this.name;
    }

    /**
     * Returns the name of the appender.
     * 
     * @return The name of the appender
     */
    public Optional<String> getOptionalName() {
        return Optional.ofNullable(this.name);
    }

    /**
     * Sets the name of the level.
     * 
     * @param name The name of the level
     */
    public void setName(
            final @Nullable String name) {
        this.name = name;
    }

    /**
     * Sets the name of the level.
     * 
     * @param name The name of the level
     * @return This instance, for method chaining
     */
    public ManagedAppenderConfig withName(
            final @Nullable String name) {
        setName(name);
        return this;
    }

    /**
     * Returns the name of the destination file of the appender.
     * 
     * @return The name of the destination file of the appender
     */
    public @Nullable String getFilename() {
        return this.filename;
    }

    /**
     * Returns the name of the destination file of the appender.
     * 
     * @return The name of the destination file of the appender
     */
    public Optional<String> getOptionalFilename() {
        return Optional.ofNullable(this.filename);
    }

    /**
     * Sets the name of the destination file of the appender.
     * 
     * @param filename The name of the destination file of the appender
     */
    public void setFilename(
            final @Nullable String filename) {
        this.filename = filename;
    }

    /**
     * Sets the name of the destination file of the appender.
     * 
     * @param filename The name of the destination file of the appender
     * @return This instance, for method chaining
     */
    public ManagedAppenderConfig withFilename(
            final @Nullable String filename) {
        setFilename(filename);
        return this;
    }

    /**
     * Returns the output format of the appender.
     * 
     * @return The output format of the appender
     */
    public @Nullable String getFormat() {
        return this.format;
    }

    /**
     * Returns the output format of the appender.
     * 
     * @return The output format of the appender
     */
    public Optional<String> getOptionalFormat() {
        return Optional.ofNullable(this.format);
    }

    /**
     * Sets the output format of the appender.
     * 
     * @param filename The output format of the appender
     */
    public void setFormat(
            final @Nullable String format) {
        this.format = format;
    }

    /**
     * Sets the output format of the appender.
     * 
     * @param filename The output format of the appender
     * @return This instance, for method chaining
     */
    public ManagedAppenderConfig withFormat(
            final @Nullable String format) {
        setFormat(format);
        return this;
    }

    /**
     * Returns the output charset of the appender.
     * 
     * @return The output charset of the appender
     */
    public @Nullable Charset getCharset() {
        return this.charset == null ? null : Charset.forName(this.charset);
    }

    /**
     * Returns the output charset of the appender.
     * 
     * @return The output charset of the appender
     */
    public Optional<Charset> getOptionalCharset() {
        return Optional.ofNullable(this.charset)
                .map(Charset::forName);
    }

    /**
     * Sets the output charset of the appender.
     * 
     * @param charset The output charset of the appender
     */
    public void setCharset(
            final @Nullable Charset charset) {
        this.charset = charset == null ? null : charset.name();
    }

    /**
     * Sets the output charset of the appender.
     * 
     * @param charset The output charset of the appender
     * @return This instance, for method chaining
     */
    public ManagedAppenderConfig withCharset(
            final @Nullable Charset charset) {
        setCharset(charset);
        return this;
    }

    /**
     * Returns the file rolling policy.
     * 
     * @return The file rolling policy
     */
    public @Nullable FileRollingPolicy getFileRollingPolicy() {
        return this.fileRollingPolicy;
    }

    /**
     * Returns the file rolling policy.
     * 
     * @return The file rolling policy
     */
    public Optional<FileRollingPolicy> getOptionalFileRollingPolicy() {
        return Optional.ofNullable(this.fileRollingPolicy);
    }

    /**
     * Sets the file rolling policy.
     * 
     * @param policy The file rolling policy
     */
    public void setFileRollingPolicy(
            final @Nullable FileRollingPolicy fileRollingPolicy) {
        this.fileRollingPolicy = fileRollingPolicy;
    }

    /**
     * Sets the file rolling policy.
     * 
     * @param policy The file rolling policy
     * @return This builder, for method chaining
     */
    public ManagedAppenderConfig withFileRollingPolicy(
            final @Nullable FileRollingPolicy policy) {
        setFileRollingPolicy(policy);
        return this;
    }

    /**
     * Creates a copy of this instance.
     * 
     * @return The created instance
     */
    public ManagedAppenderConfig copy() {
        return new ManagedAppenderConfig(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.name,
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
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ManagedAppenderConfig other = (ManagedAppenderConfig) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.filename, other.filename)
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
                "ManagedAppenderConfig [name=%s]",
                this.getOptionalName());
    }
}
