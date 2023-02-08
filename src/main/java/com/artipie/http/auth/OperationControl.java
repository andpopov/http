/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.auth;

import com.artipie.security.policy.Policy;
import java.security.Permission;

/**
 * Operation controller for slice. The class is meant to check
 * if required permission is granted for user.
 *
 * Instances of this class are created in the adapter with users' policies and required
 * permission for the adapter' operation.
 * @since 1.2
 */
public final class OperationControl {

    /**
     * Security policy.
     */
    private final Policy<?> policy;

    /**
     * Required permission.
     */
    private final Permission perm;

    /**
     * Ctor.
     * @param policy Security policy
     * @param perm Required permission
     */
    public OperationControl(final Policy<?> policy, final Permission perm) {
        this.policy = policy;
        this.perm = perm;
    }

    /**
     * Check if user is authorized to perform an action.
     * @param user User name
     * @return True if authorized
     */
    public boolean allowed(final Authentication.User user) {
        return this.policy.getPermissions(user.name()).implies(this.perm);
    }
}
