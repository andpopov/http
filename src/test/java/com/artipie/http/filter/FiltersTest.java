/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.filter;

import com.artipie.http.Headers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * Test for {@link Filters}.
 *
 * @since 1.2
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class FiltersTest {
    /**
     * Request path.
     */
    private static final String PATH = "/mvnrepo/com/artipie/inner/0.1/inner-0.1.pom";

    @Test
    void emptyFilterLists() {
        final Filters filters = FiltersTest.createFilters(
            String.join(
                System.lineSeparator(),
                "filters:",
                "  include:",
                "  exclude:"
            )
        );
        MatcherAssert.assertThat(
            filters.allowed(FiltersTestUtil.get(FiltersTest.PATH), Headers.EMPTY),
            IsNot.not(new IsTrue())
        );
    }

    @Test
    void allows() {
        final Filters filters = FiltersTest.createFilters(
            String.join(
                System.lineSeparator(),
                "include:",
                "  - type: glob",
                "    filter: **/com/acme/**",
                "  - type: glob",
                "    filter: **/com/artipie/**",
                "exclude:",
                "  - type: glob",
                "    filter: **/org/log4j/**"
            )
        );
        MatcherAssert.assertThat(
            filters.allowed(FiltersTestUtil.get(FiltersTest.PATH), Headers.EMPTY),
            new IsTrue()
        );
    }

    @Test
    void forbidden() {
        final Filters filters = FiltersTest.createFilters(
            String.join(
                System.lineSeparator(),
                "include:",
                "  - type: glob",
                "    filter: **/*",
                "exclude:",
                "  - type: glob",
                "    filter: **/com/artipie/**"
            )
        );
        MatcherAssert.assertThat(
            filters.allowed(FiltersTestUtil.get(FiltersTest.PATH), Headers.EMPTY),
            IsNot.not(new IsTrue())
        );
    }

    /**
     * Creates Filters instance from yaml configuration.
     * @param yaml Yaml configuration for filters.
     * @return Filters
     */
    private static Filters createFilters(final String yaml) {
        return new Filters(FiltersTestUtil.yaml(yaml));
    }
}
