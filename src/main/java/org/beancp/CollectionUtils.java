/*
 * bean-cp
 * Copyright (c) 2014, Rafal Chojnacki, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package org.beancp;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Common functions for collections.
 */
public class CollectionUtils {

    private CollectionUtils() {
        throw new IllegalStateException("Not allowed to create instance of this class");
    }

    /**
     * Returns first value from {@code args} which is not equal to null. If all arguments are equal
     * to null then will return null.
     *
     * @param <T> result type.
     * @param args arguments used to find not null value.
     *
     * @return first value from {@code args} which is not equal to null or null if all arguments are
     * equal to null
     */
    public static <T> T firstNotNullOrNull(final T... args) {
        for (T i : args) {
            if (i != null) {
                return i;
            }
        }

        return null;
    }

    /**
     * Returns first value in {@code collection} which is not equal to null and matches filter. If
     * there is not such element then will return null.
     *
     * @param <T> result type.
     * @param collection collection to search in.
     * @param filter filter predicate.
     * @return first value in {@code collection} which is not equal to null and matches filter, if
     * there is not such element then will return null.
     */
    public static <T> T firstOrNull(final Collection<T> collection, final Predicate<T> filter) {
        Optional<T> findFirst = collection
                .stream()
                .filter(filter)
                .findFirst();

        return (findFirst.isPresent() ? findFirst.get() : null);
    }
}
