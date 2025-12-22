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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.test.rnd.Generators;

/**
 * Unit tests for {@link FileRollingPolicyTemplate}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-12
 * @since 1.0
 */
@Tag("ut")
class FileRollingPolicyTemplateTest extends BaseBeanTest<FileRollingPolicyTemplate> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected FileRollingPolicyTemplate createInstance() {
        return Generators.randomValue(FileRollingPolicyTemplate.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FileRollingPolicyTemplate createCopy(
            final FileRollingPolicyTemplate bean) {
        return bean.copy().build();
    }

    /**
     * Tests that the template code is required.
     */
    @Test
    void testRequiredCode() {
        final FileRollingPolicyTemplate.Builder builder = FileRollingPolicyTemplate.builder()
                .withName("Daily Rolling Policy")
                .withPolicy(TimeBasedFileRollingPolicy.builder()
                        .withPeriod(RollingPeriod.DAILY)
                        .build());
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the template name is required.
     */
    @Test
    void testRequiredName() {
        final FileRollingPolicyTemplate.Builder builder = FileRollingPolicyTemplate.builder()
                .withCode("DAILY")
                .withPolicy(TimeBasedFileRollingPolicy.builder()
                        .withPeriod(RollingPeriod.DAILY)
                        .build());
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the template policy is required.
     */
    @Test
    void testRequiredPolicy() {
        final FileRollingPolicyTemplate.Builder builder = FileRollingPolicyTemplate.builder()
                .withCode("DAILY")
                .withName("Daily Rolling Policy");
        assertThrows(NullPointerException.class, builder::build);
    }

    /**
     * Tests that the template builder assigns all properties.
     */
    @Test
    void testBuilder() {
        final FileRollingPolicy policy = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final FileRollingPolicyTemplate bean = FileRollingPolicyTemplate.builder()
                .withCode("DAILY")
                .withName("Daily Rolling Policy")
                .withPolicy(policy)
                .build();
        assertEquals("DAILY", bean.getCode());
        assertEquals("Daily Rolling Policy", bean.getName());
        assertEquals(policy, bean.getPolicy());
    }

    /**
     * Tests that {@link FileRollingPolicyTemplate#newPolicy()} creates a new
     * policy builder copy of template's policy.
     */
    @Test
    void testNewPolicy() {
        final FileRollingPolicy policyTemplate = TimeBasedFileRollingPolicy.builder()
                .withPeriod(RollingPeriod.DAILY)
                .build();
        final FileRollingPolicyTemplate bean = FileRollingPolicyTemplate.builder()
                .withCode("DAILY")
                .withName("Daily Rolling Policy")
                .withPolicy(policyTemplate)
                .build();
        assertNotSame(policyTemplate, bean.newPolicy().build());
        assertEquals(policyTemplate, bean.newPolicy().build());
        assertEquals(
                TimeBasedFileRollingPolicy.builder()
                    .withPeriod(RollingPeriod.DAILY)
                    .withMaxHistory(20)
                    .build(),
                bean.newPolicy()
                    .withMaxHistory(20)
                    .build());
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
        final FileRollingPolicyTemplate original = FileRollingPolicyTemplate.builder()
                .withCode("DAILY")
                .withName("Daily Rolling Policy")
                .withPolicy(policy)
                .build();
        assertNotEquals(original, original.copy()
                .withCode("OTHER_CODE")
                .build());
        assertNotEquals(original, original.copy()
                .withName("Oher policy template name")
                .build());
        assertNotEquals(original, original.copy()
                .withPolicy(otherPolicy)
                .build());
    }

    /**
     * Tests that the generation of test instances is supported.
     */
    @Test
    void testGenerable() {
        assertNotNull(Generators.defaultValue(FileRollingPolicyTemplate.class));
        assertNotNull(Generators.randomValue(FileRollingPolicyTemplate.class));
    }
}
