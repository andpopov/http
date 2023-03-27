/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.security.perms;

import com.amihaiemil.eoyaml.Scalar;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import com.artipie.asto.factory.Config;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Permission configuration.
 * @since 1.2
 */
public interface PermissionConfig extends Config {

    /**
     * Gets sequence of keys.
     *
     * @return Keys sequence.
     */
    Set<String> keys();

    /**
     * Yaml permission config.
     * Implementation note:
     * Yaml permission config allows {@link AdapterBasicPermission#WILDCARD} yaml sequence.In
     * yamls `*` sign can be quoted. Thus, we need to handle various quotes properly.
     * @since 1.2
     */
    final class Yaml implements PermissionConfig {

        /**
         * Yaml mapping to read permission from.
         */
        private final YamlMapping yaml;

        /**
         * Ctor.
         * @param yaml Yaml mapping to read permission from
         */
        public Yaml(final YamlMapping yaml) {
            this.yaml = yaml;
        }

        @Override
        public String string(final String key) {
            return this.yaml.string(key);
        }

        @Override
        public Set<String> sequence(final String key) {
            final Set<String> res;
            if (AdapterBasicPermission.WILDCARD.equals(key)) {
                res = this.yaml.yamlSequence(this.getWildcardKey(key)).values().stream()
                    .map(item -> item.asScalar().value()).collect(Collectors.toSet());
            } else {
                res = this.yaml.yamlSequence(key).values().stream().map(
                    item -> item.asScalar().value()
                ).collect(Collectors.toSet());
            }
            return res;
        }

        @Override
        public Set<String> keys() {
            return this.yaml.keys().stream().map(node -> node.asScalar().value())
                .map(Yaml::cleanName).collect(Collectors.toSet());
        }

        @Override
        public PermissionConfig config(final String key) {
            final PermissionConfig res;
            if (AdapterBasicPermission.WILDCARD.equals(key)) {
                res = new Yaml(this.yaml.yamlMapping(this.getWildcardKey(key)));
            } else {
                res = new Yaml(this.yaml.yamlMapping(key));
            }
            return res;
        }

        /**
         * Find wildcard key as it can be escaped in various ways.
         * @param key The key
         * @return Escaped key to get sequence or mapping with it
         */
        private Scalar getWildcardKey(final String key) {
            return this.yaml.keys().stream().map(YamlNode::asScalar).filter(
                item -> item.value().contains(AdapterBasicPermission.WILDCARD)
            ).findFirst().orElseThrow(
                () -> new IllegalStateException(
                    String.format("Sequence %s not found", key)
                )
            );
        }

        /**
         * Cleans wildcard value from various escape signs.
         * @param value Value to check and clean
         * @return Cleaned value
         */
        private static String cleanName(final String value) {
            String res = value;
            if (value.contains(AdapterBasicPermission.WILDCARD)) {
                res = value.replace("\"", "").replace("'", "").replace("\\", "");
            }
            return res;
        }
    }
}
