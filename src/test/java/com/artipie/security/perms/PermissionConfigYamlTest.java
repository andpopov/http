/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.security.perms;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link PermissionConfig.Yaml}.
 * Yaml permissions format example:
 * <pre>{@code
 *   # permissions for some role
 *
 *   java-devs:
 *     adapter_basic_permission:
 *       maven-repo:
 *         - read
 *         - write
 *       python-repo:
 *         - read
 *       npm-repo:
 *         - read
 *
 *   # permissions for admin
 *   admins:
 *     adapter_all_permission: {}
 * }</pre>
 * {@link PermissionConfig.Yaml} implementation will receive mapping for single permission
 * adapter_basic_permission instance, for example:
 * <pre>{@code
 * maven-repo:
 *   - read
 *   - write
 * }</pre>
 * @since 1.2
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class PermissionConfigYamlTest {

    @Test
    void readsName() throws IOException {
        MatcherAssert.assertThat(
            new PermissionConfig.Yaml(
                Yaml.createYamlInput(
                    String.join(
                        "\n",
                        "maven-repo:",
                        "  - read"
                    )
                ).readYamlMapping()
            ).name(),
            new IsEqual<>("maven-repo")
        );
    }

    @Test
    void readsSequence() throws IOException {
        MatcherAssert.assertThat(
            new PermissionConfig.Yaml(
                Yaml.createYamlInput(
                    String.join(
                        "\n",
                        "some-repo:",
                        "  - read",
                        "  - write",
                        "  - delete"
                    )
                ).readYamlMapping()
            ).sequence("some-repo"),
            Matchers.contains("read", "write", "delete")
        );
    }

    @Test
    void readValueByKey() throws IOException {
        MatcherAssert.assertThat(
            new PermissionConfig.Yaml(
                Yaml.createYamlInput(
                    String.join(
                        "\n",
                        "some-repo:",
                        "  key: value",
                        "  key1: value2"
                    )
                ).readYamlMapping()
            ).value("key"),
            new IsEqual<>("value")
        );
    }

    @Test
    void returnsOriginalForm() throws IOException {
        final YamlMapping yaml = Yaml.createYamlInput(
            String.join(
                "\n",
                "docker-repo:",
                "  repository:",
                "    my-alpine:",
                "      - pull",
                "    ubuntu-slim:",
                "      - pull",
                "      - push",
                "  registry:",
                "    base:",
                "      - \"*\""
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            new PermissionConfig.Yaml(yaml).original(),
            new IsEqual<>(yaml)
        );
    }
}
