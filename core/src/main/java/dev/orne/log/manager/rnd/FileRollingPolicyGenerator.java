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

import org.apiguardian.api.API;

import dev.orne.log.manager.FileRollingPolicy;
import dev.orne.log.manager.RollingPeriod;
import dev.orne.log.manager.SizeAndTimeBasedFileRollingPolicy;
import dev.orne.log.manager.SizeBasedFileRollingPolicy;
import dev.orne.log.manager.TimeBasedFileRollingPolicy;
import dev.orne.test.rnd.AbstractTypedGenerator;
import dev.orne.test.rnd.Generators;

/**
 * Generator of {@code FileRollingPolicy} for testing purposes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0.0")
public class FileRollingPolicyGenerator
extends AbstractTypedGenerator<FileRollingPolicy> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FileRollingPolicy defaultValue() {
        return TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileRollingPolicy randomValue() {
        final FileRollingPolicy.Type type = Generators.randomValue(
                FileRollingPolicy.Type.class);
        switch (type) {
            case SIZE_BASED:
                return Generators.randomValue(
                        SizeBasedFileRollingPolicy.class);
            case TIME_BASED:
                return Generators.randomValue(
                        TimeBasedFileRollingPolicy.class);
            default: // SIZE_AND_TIME_BASED
                return Generators.randomValue(
                        SizeAndTimeBasedFileRollingPolicy.class);
        }
    }
}
