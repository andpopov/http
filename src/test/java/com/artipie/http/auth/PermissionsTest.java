/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.auth;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for {@link Permissions}.
 *
 * @since 0.15
 */
public final class PermissionsTest {

    @Test
    void wrapDelegatesToOrigin() {
        final String name = "user";
        final String act = "act";
        final boolean result = true;
        MatcherAssert.assertThat(
            "Result is forwarded from delegate without modification",
            new TestPermissions(
                (identity, action) -> {
                    MatcherAssert.assertThat(
                        "Username is forwarded to delegate without modification",
                        identity,
                        new IsEqual<>(new AuthUser(name))
                    );
                    MatcherAssert.assertThat(
                        "Action is forwarded to delegate without modification",
                        action,
                        new IsEqual<>(act)
                    );
                    return result;
                }
            ).allowed(new AuthUser(name), act),
            new IsEqual<>(result)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "Aladdin,read,true",
        "Aladdin,write,false",
        "JohnDoe,read,false"
    })
    void singleAllowsAsExpected(
        final String username,
        final String action,
        final boolean allow
    ) {
        MatcherAssert.assertThat(
            new Permissions.Single("Aladdin", "read")
                .allowed(new AuthUser(username), action),
            new IsEqual<>(allow)
        );
    }

    /**
     * Permissions for testing Permissions.Wrap.
     *
     * @since 0.15
     */
    private static final class TestPermissions extends Permissions.Wrap {

        /**
         * Ctor.
         *
         * @param origin Origin permissions.
         */
        TestPermissions(final Permissions origin) {
            super(origin);
        }
    }
}
