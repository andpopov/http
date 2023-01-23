/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.security.policy;

import com.amihaiemil.eoyaml.Yaml;
import com.artipie.asto.blocking.BlockingStorage;
import com.artipie.asto.factory.Config;
import com.artipie.asto.factory.StoragesLoader;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Policy factory to create {@link YamlPolicy}. Yaml policy is read from storage, and it's required
 * to describe this storage in the configuration. Configuration format is the following:
 *
 * policy:
 *   type: yaml_policy
 *   storage:
 *     type: fs
 *     path: /some/path
 *
 * The storage itself is expected to have yaml files with permissions in the following structure:
 *
 * ..
 * ├── roles.yaml
 * ├── users
 * │   ├── david.yaml
 * │   ├── jane.yaml
 * │   ├── ...
 *
 * @since 1.2
 */
@ArtipiePolicyFactory("yaml_policy")
public final class YamlPolicyFactory implements PolicyFactory {

    @Override
    public Policy<?> getPolicy(final Config config) {
        final Config sub = config.config("storage");
        try {
            return new YamlPolicy(
                new BlockingStorage(
                    new StoragesLoader().newObject(
                        sub.string("type"),
                        new Config.YamlStorageConfig(
                            Yaml.createYamlInput(sub.toString()).readYamlMapping()
                        )
                    )
                )
            );
        } catch (final IOException err) {
            throw new UncheckedIOException(err);
        }
    }
}
