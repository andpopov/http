/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.filter;

import com.amihaiemil.eoyaml.YamlMapping;
import com.artipie.http.rq.RequestLineFrom;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * RegExp repository filter.
 *
 * @since 1.2
 */
public final class RegexpFilter implements Filter {
    /**
     * Path regexp pattern.
     */
    private final Pattern pattern;

    /**
     * Ctor.
     * @param yaml Yaml mapping to read filters from
     */
    @SuppressWarnings(
        {"PMD.ConstructorOnlyInitializesOrCallOtherConstructors", "PMD.AvoidDuplicateLiterals"}
    )
    public RegexpFilter(final YamlMapping yaml) {
        if (Boolean.parseBoolean(yaml.string("case_insensitive"))) {
            this.pattern = Pattern.compile(yaml.string("filter"), Pattern.CASE_INSENSITIVE);
        } else {
            this.pattern = Pattern.compile(yaml.string("filter"));
        }
    }

    @Override
    public boolean check(final RequestLineFrom line,
        final Iterable<Map.Entry<String, String>> headers) {
        return this.pattern.matcher(line.uri().getPath()).matches();
    }
}
