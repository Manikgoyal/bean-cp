/*
 * ObjectMapper4j
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

public class MapSelectionTest {

    public static class Source {

        private String a;

        private String b;

        private String x;

        private String y;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }

    public static class Destination {

        private String a;

        private String b;

        private String x;

        private String y;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }

    public static class InheritedFromSource extends Source {
    }

    public static class InheritedFromDestination extends Destination {
    }

    @Test
    public void exactly_matching_mapper_should_be_used_when_available() {
        // GIVEN
        InheritedFromSource source = new InheritedFromSource();
        source.setX("xval");

        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "4", (v) -> ref.destination().setA(v)))
                .addMap(InheritedFromSource.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "3", (v) -> ref.destination().setA(v)))
                .addMap(InheritedFromSource.class, InheritedFromDestination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "1", (v) -> ref.destination().setA(v)))
                .addMap(Source.class, InheritedFromDestination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "2", (v) -> ref.destination().setA(v)))
                .buildMapper();

        // WHEN
        InheritedFromDestination result = mapper.map(source, InheritedFromDestination.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval1", result.getA());
    }

    @Test
    public void map_with_exact_destination_class_has_higher_priority_than_with_exact_source_class() {
        // GIVEN
        InheritedFromSource source = new InheritedFromSource();
        source.setX("xval");

        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "4", (v) -> ref.destination().setA(v)))
                .addMap(Source.class, InheritedFromDestination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "2", (v) -> ref.destination().setA(v)))
                .addMap(InheritedFromSource.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "3", (v) -> ref.destination().setA(v)))
                .buildMapper();

        // WHEN
        InheritedFromDestination result = mapper.map(source, InheritedFromDestination.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval2", result.getA());
    }

    @Test
    public void map_with_exact_source_class_has_higher_priority_than_with_no_exact_class_at_all() {
        // GIVEN
        InheritedFromSource source = new InheritedFromSource();
        source.setX("xval");

        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "4", (v) -> ref.destination().setA(v)))
                .addMap(InheritedFromSource.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "3", (v) -> ref.destination().setA(v)))
                .buildMapper();

        // WHEN
        InheritedFromDestination result = mapper.map(source, InheritedFromDestination.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval3", result.getA());
    }

    @Test
    public void map_shoudl_accept_inherited_classes_by_default_if_more_specific_option_is_not_available() {
        // GIVEN
        InheritedFromSource source = new InheritedFromSource();
        source.setX("xval");

        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class,
                        (config, ref)
                        -> config.bindFunction(
                                () -> ref.source().getX() + "4", (v) -> ref.destination().setA(v)))
                .buildMapper();

        // WHEN
        InheritedFromDestination result = mapper.map(source, InheritedFromDestination.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval4", result.getA());
    }
}
