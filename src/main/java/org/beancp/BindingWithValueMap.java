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

import static org.apache.commons.lang3.Validate.*;

/**
 * Biding with additional source value mapping (before it is set to destination).
 */
public class BindingWithValueMap extends Binding {

    /**
     * Creates binding from series of bindings from source (AKA path) to destination member.
     *
     * @param sourcePath series of bindings from source where n+1 binding returns field or property
     * value from object returned by n-th binding.
     * @param destinationMember destination member.
     */
    public BindingWithValueMap(
            final BindingSide[] sourcePath, final BindingSide destinationMember) {
        super(sourcePath, destinationMember);
    }

    /**
     * Creates biding from source member to destination member.
     *
     * @param sourceMember source member.
     * @param destinationMember destination member.
     */
    public BindingWithValueMap(
            final BindingSide sourceMember, final BindingSide destinationMember) {
        super(sourceMember, destinationMember);
    }

    /**
     * Sets value at destination.
     *
     * @param mapper caller.
     * @param destination destination object.
     * @param value value to set.
     */
    @Override
    protected void setValueAtDestination(
            final Mapper mapper, final Object destination, final Object value) {
        notNull(mapper, "mapper");
        notNull(destination, "destination");

        BindingSide destinationMember = getDestinationMember();

        if (destinationMember.isGetterAvailable()) {
            Object currentValue = destinationMember.getValue(destination);

            if (currentValue != null) {
                mapper.map(value, currentValue);
            } else {
                Object mapResult = mapper.map(value, destinationMember.getValueClass());
                super.setValueAtDestination(mapper, destination, mapResult);
            }
        } else {
            Object mapResult = mapper.map(value, destinationMember.getValueClass());
            super.setValueAtDestination(mapper, destination, mapResult);
        }
    }
}
