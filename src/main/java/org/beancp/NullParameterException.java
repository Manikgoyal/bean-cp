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

/**
 * Indicates null value for method parameter which do not allow nulls.
 */
public class NullParameterException extends RuntimeException {

    /**
     * Will throw {@link NullParameterException} if {@code parameterValue} is null.
     *
     * @param parameterValue parameter value to check.
     * @param parameterName parameter name.
     */
    public static void failIfNull(Object parameterValue, String parameterName) {
        if (parameterValue == null) {
            throw new NullParameterException(parameterName);
        }
    }

    /**
     * Constructs an instance of <code>MapDefinitionException</code> with the specified parameter
     * name.
     *
     * @param parameterName parameter name
     */
    public NullParameterException(String parameterName) {
        super(String.format("Null not allowed for '%s' parameter.", parameterName));
    }
}
