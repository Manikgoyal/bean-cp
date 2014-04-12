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

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructDestinationObjectUsingTest {

    public static class Source {

        private int x;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }

    public static class Destination {

        private int a, b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static class InheritedFromDestination extends Destination {
    }

    @Test
    public void constructDestinationObjectUsing_should_be_used_to_construct_destination_object_if_available() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(2);

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .constructDestinationObjectUsing(() -> {
                    Destination newDestination = new Destination();
                    newDestination.setB(7);

                    return newDestination;
                })
                .bind(source::getX, destination::setA))
                .buildMapper();

        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals("Invalid 'a' value.", 2, result.getA());
        assertEquals("Invalid 'b' value.", 7, result.getB());
    }

    @Test(expected = MappingException.class)
    public void when_constructDestinationObjectUsing_returned_other_method_that_expected_the_mapper_should_throw_exception() {
        // GIVEN
        Source sourceInstance = new Source();

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .constructDestinationObjectUsing(() -> {
                    Destination newDestination = new Destination();
                    newDestination.setB(7);

                    return newDestination;
                }))
                .buildMapper();

        mapper.map(sourceInstance, InheritedFromDestination.class);

        // THEN: exception expected
    }

    @Test
    public void when_constructDestinationObjectUsing_returned_inherited_class_the_should_be_accepted() {
        // GIVEN
        Source sourceInstance = new Source();

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .constructDestinationObjectUsing(() -> {
                    InheritedFromDestination newDestination = new InheritedFromDestination();
                    newDestination.setB(7);

                    return newDestination;
                }))
                .buildMapper();

        mapper.map(sourceInstance, Destination.class);

        // THEN: exception expected
    }

    @Test(expected = MapConfigurationException.class)
    public void constructDestinationObjectUsing_line_must_be_before_beforeMap_line() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(2);

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .beforeMap(() -> {
                })
                .constructDestinationObjectUsing(() -> {
                    Destination newDestination = new Destination();
                    newDestination.setB(7);

                    return newDestination;
                })
                .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: exception exptected
    }

    @Test(expected = MapConfigurationException.class)
    public void constructDestinationObjectUsing_line_must_be_before_bind_line() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(2);

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .bind(source::getX, destination::setA)
                .constructDestinationObjectUsing(() -> {
                    Destination newDestination = new Destination();
                    newDestination.setB(7);

                    return newDestination;
                }))
                .buildMapper();

        // THEN: exception exptected
    }

    @Test(expected = MapConfigurationException.class)
    public void constructDestinationObjectUsing_line_must_be_before_bindConstant_line() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(2);

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .bindConstant(9, destination::setA)
                .constructDestinationObjectUsing(() -> {
                    Destination newDestination = new Destination();
                    newDestination.setB(7);

                    return newDestination;
                }))
                .buildMapper();

        // THEN: exception exptected
    }

    @Test(expected = MapConfigurationException.class)
    public void constructDestinationObjectUsing_line_must_be_before_afterMap_line() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(2);

        // WHEN
        Mapper mapper = new MapperBuilder().addMap(Source.class, Destination.class,
                (config, source, destination) -> config
                .afterMap(() -> {
                })
                .constructDestinationObjectUsing(() -> {
                    Destination newDestination = new Destination();
                    newDestination.setB(7);

                    return newDestination;
                })
                .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: exception exptected
    }
}
