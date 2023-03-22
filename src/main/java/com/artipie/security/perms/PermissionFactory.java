/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.security.perms;

import java.security.Permission;
import java.util.Collection;

/**
 * Permission factory to create permission instance.
 * @since 1.2
 */
public interface PermissionFactory {

    /**
     * Create permission instance.
     * @param config Configuration
     * @return Config
     */
    Collection<Permission> newPermission(PermissionConfig<?> config);
}
