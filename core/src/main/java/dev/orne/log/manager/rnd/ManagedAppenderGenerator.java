package dev.orne.log.manager.rnd;

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
import java.nio.charset.StandardCharsets;

import org.apiguardian.api.API;

import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.ManagedAppender;
import dev.orne.test.rnd.AbstractTypedGenerator;
import dev.orne.test.rnd.Generators;

/**
 * Generator of {@code ManagedAppender} for testing purposes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class ManagedAppenderGenerator
extends AbstractTypedGenerator<ManagedAppender> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagedAppender defaultValue() {
        return ManagedAppender.builder()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagedAppender randomValue() {
        return ManagedAppender.builder()
                .withName(Generators.randomValue(String.class))
                .withFilename(Generators.randomValue(String.class))
                .withFormat(Generators.randomValue(String.class))
                .withCharset(Generators.randomValue(Charset.class))
                .withFileRollingPolicy(Generators.nullableRandomValue(FileRollingPolicy.class))
                .build();
    }
}
