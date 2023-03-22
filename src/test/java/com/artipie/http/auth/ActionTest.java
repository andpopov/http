/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.auth;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test for {@link Action.ByString}.
 * @since 0.16
 */
class ActionTest {

    @ParameterizedTest
    @CsvSource({
        "r,READ",
        "read,READ",
        "download,READ",
        "install,READ",
        "w,WRITE",
        "write,WRITE",
        "upload,WRITE",
        "publish,WRITE",
        "push,WRITE",
        "deploy,WRITE",
        "d,DELETE",
        "delete,DELETE"
    })
    void parses(final String name, final Action.Standard action) {
        MatcherAssert.assertThat(
            new Action.ByString(name).get(),
            new IsEqual<>(action)
        );
    }

    @Test
    void throwsExceptionOnUnknownAction() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Action.ByString("anything").get()
        );
    }

}
