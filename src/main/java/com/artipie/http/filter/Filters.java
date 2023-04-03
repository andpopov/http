/*
 * The MIT License (MIT) Copyright (c) 2020-2023 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.filter;

import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import com.artipie.http.rq.RequestLineFrom;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Filters.
 *
 * @since 1.2
 */
public final class Filters {
    /**
     * Filter factory loader.
     */
    private static final FilterFactoryLoader FILTER_LOADER = new FilterFactoryLoader();

    /**
     * Including filters.
     */
    private final List<Filter> includes;

    /**
     * Excluding filters.
     */
    private final List<Filter> excludes;

    /**
     * Ctor.
     * @param yaml Yaml mapping to read filters from
     */
    public Filters(final YamlMapping yaml) {
        this.includes = Filters.readFilterList(yaml, "include", Filters.FILTER_LOADER);
        this.excludes = Filters.readFilterList(yaml, "exclude", Filters.FILTER_LOADER);
    }

    /**
     * Whether allowed to get access to repository content.
     * @param line Request line
     * @param headers Request headers.
     * @return True if is allowed to get access to repository content.
     */
    public boolean allowed(final String line,
        final Iterable<Map.Entry<String, String>> headers) {
        final RequestLineFrom rqline = new RequestLineFrom(line);
        final boolean included = this.includes.stream()
            .anyMatch(filter -> filter.check(rqline, headers));
        final boolean excluded = this.excludes.stream()
            .anyMatch(filter -> filter.check(rqline, headers));
        return included & !excluded;
    }

    /**
     * Total number of filters.
     * @return Number of filters.
     */
    public int size() {
        return this.includes.size() + this.excludes.size();
    }

    /**
     * Reads yaml definitions of filters.
     * @param yaml Yaml-mapping
     * @param property Property name
     * @param loader FilterFactoryLoader
     * @return List of filters
     */
    private static List<Filter> readFilterList(final YamlMapping yaml, final String property,
        final FilterFactoryLoader loader) {
        return Optional.ofNullable(yaml.yamlSequence(property))
            .map(
                sequence -> sequence.values().stream()
                    .map(YamlNode::asMapping)
                    .map(node -> loader.newObject(node.string("type"), node))
                    .collect(Collectors.toList())
            )
            .orElse(Collections.emptyList());
    }
}
