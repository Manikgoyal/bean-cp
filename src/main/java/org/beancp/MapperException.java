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
 * Indicates error during mapping.
 */
public class MapperException extends RuntimeException {

    /**
     * Creates a new instance of <code>MappingException</code> without detail message.
     */
    public MapperException() {
    }

    /**
     * Constructs an instance of <code>MappingException</code> with the specified detail
     * message.
     *
     * @param message The detail message.
     */
    public MapperException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>MappingException</code> with the specified inner
     * exception.
     *
     * @param throwable The inner exception.
     */
    public MapperException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructs an instance of <code>MappingException</code> with the specified detail
     * message and inner exception.
     *
     * @param message The detail message.
     * @param throwable The inner exception.
     */
    public MapperException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
