/*
 * MIT License
 *
 * Copyright (c) 2020 Artipie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.http.rs;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * HTTP header.
 * Name of header is considered to be case-insensitive when compared to one another.
 *
 * @since 0.8
 */
public final class Header implements Map.Entry<String, String> {

    /**
     * Name.
     */
    private final String name;

    /**
     * Value.
     */
    private final String value;

    /**
     * Ctor.
     *
     * @param entry Entry representing a header.
     */
    public Header(final Map.Entry<String, String> entry) {
        this(entry.getKey(), entry.getValue());
    }

    /**
     * Ctor.
     *
     * @param name Name.
     * @param value Value.
     */
    public Header(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String setValue(final String ignored) {
        throw new UnsupportedOperationException("Value cannot be modified");
    }

    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        final Header header = (Header) that;
        return this.lowercaseName().equals(header.lowercaseName())
            && this.value.equals(header.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lowercaseName(), this.value);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.name, this.value);
    }

    /**
     * Converts name to lowercase for comparison.
     *
     * @return Name in lowercase.
     */
    private String lowercaseName() {
        return this.name.toLowerCase(Locale.US);
    }
}
