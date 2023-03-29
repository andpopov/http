/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.filter;

import com.artipie.http.rq.RequestLineFrom;
import java.util.Map;
import java.util.Objects;

/**
 * Repository content filter.
 * @since 1.2
 */
public interface Filter {
    /**
     * Checks conditions to get access to repository content.
     *
     * @param line Request line
     * @param headers Request headers.
     * @return True if request matched to access conditions.
     */
    boolean check(RequestLineFrom line, Iterable<Map.Entry<String, String>> headers);

    /**
     * Wrap is a decorative wrapper for Filter.
     *
     * @since 0.7
     */
    abstract class Wrap implements Filter {

        /**
         * Origin filter.
         */
        private final Filter filter;

        /**
         * Ctor.
         *
         * @param filter Filter.
         */
        protected Wrap(final Filter filter) {
            this.filter = Objects.requireNonNull(filter);
        }

        @Override
        public boolean check(final RequestLineFrom line,
            final Iterable<Map.Entry<String, String>> headers) {
            return this.filter.check(line, headers);
        }
    }
}
