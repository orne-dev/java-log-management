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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@link ManagedAppenderConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class ManagedAppenderConfigTest extends BaseBeanTest<ManagedAppenderConfig> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ManagedAppenderConfig createInstance() {
        return Generators.randomValue(ManagedAppenderConfig.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ManagedAppenderConfig createCopy(
            final ManagedAppenderConfig bean) {
        return bean.copy();
    }

    /**
     * Tests that the appender configuration can be used as standard
     * Java Bean.
     */
    @Test
    void testJavaBean()
    throws ReflectiveOperationException{
        final FileRollingPolicy policy = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final ManagedAppenderConfig bean = new ManagedAppenderConfig();
        assertNull(PropertyUtils.getProperty(bean, "name"));
        assertNull(PropertyUtils.getProperty(bean, "filename"));
        assertNull(PropertyUtils.getProperty(bean, "format"));
        assertNull(PropertyUtils.getProperty(bean, "charset"));
        assertNull(PropertyUtils.getProperty(bean, "fileRollingPolicy"));
        PropertyUtils.setProperty(bean, "name", "STDOUT");
        PropertyUtils.setProperty(bean, "filename", "out.log");
        PropertyUtils.setProperty(bean, "format", "%m%n");
        PropertyUtils.setProperty(bean, "charset", StandardCharsets.UTF_8);
        PropertyUtils.setProperty(bean, "fileRollingPolicy", policy);
        assertEquals("STDOUT", PropertyUtils.getProperty(bean, "name"));
        assertEquals("out.log", PropertyUtils.getProperty(bean, "filename"));
        assertEquals("%m%n", PropertyUtils.getProperty(bean, "format"));
        assertEquals(StandardCharsets.UTF_8, PropertyUtils.getProperty(bean, "charset"));
        assertEquals(policy, PropertyUtils.getProperty(bean, "fileRollingPolicy"));
    }

    /**
     * Tests that the appender configuration has fluent API to assigns all
     * properties.
     */
    @Test
    void testFluentAPI() {
        final FileRollingPolicy policy = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final ManagedAppenderConfig bean = new ManagedAppenderConfig()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8)
                .withFileRollingPolicy(policy);
        assertEquals(Optional.of("STDOUT"), bean.getOptionalName());
        assertEquals(Optional.of("out.log"), bean.getOptionalFilename());
        assertEquals(Optional.of("%m%n"), bean.getOptionalFormat());
        assertEquals(Optional.of(StandardCharsets.UTF_8), bean.getOptionalCharset());
        assertEquals(Optional.of(policy), bean.getOptionalFileRollingPolicy());
    }

    /**
     * Test that changing any property results in a non-equal instance.
     */
    @Test
    void testNotEquals() {
        final FileRollingPolicy policy = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final FileRollingPolicy otherPolicy = SizeBasedFileRollingPolicy.builder()
                .withFileSize(10L * 1024L * 1024L)
                .build();
        final ManagedAppenderConfig original = new ManagedAppenderConfig()
                .withName("STDOUT")
                .withFilename("out.log")
                .withFormat("%m%n")
                .withCharset(StandardCharsets.UTF_8)
                .withFileRollingPolicy(policy);
        assertNotEquals(original, original.copy()
                .withName("OTHER_APPENDER"));
        assertNotEquals(original, original.copy()
                .withFilename("error.log"));
        assertNotEquals(original, original.copy()
                .withFormat(LogManagementConfig.Defaults.APPENDER_FALLBACK_FORMAT));
        assertNotEquals(original, original.copy()
                .withCharset(null));
        assertNotEquals(original, original.copy()
                .withFileRollingPolicy(otherPolicy));
        assertNotEquals(original, original.copy()
                .withFileRollingPolicy(null));
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(ManagedAppenderConfig.class));
        assertNotNull(Generators.randomValue(ManagedAppenderConfig.class));
    }
}
