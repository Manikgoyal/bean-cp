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
package org.objectmapper4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTest {

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

    @Test
    public void mapper_should_work_when_mapped_properties_are_final()
            throws NoSuchFieldException {
        // GIVEN
        SourceWithFinalMembers sampleSource = new SourceWithFinalMembers();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithFinalMembers.class, DestinationWithFinalMembers.class,
                        (config, ref) -> config
                        .bindFunction(ref.source()::getX, ref.destination()::setA))
                .buildMapper();

        DestinationWithFinalMembers result = new DestinationWithFinalMembers();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
    }

    public static class SourceWithFinalMembers {

        private String x;

        protected SourceWithFinalMembers() {
        }

        public final String getX() {
            return x;
        }

        public final void setX(String x) {
            this.x = x;
        }
    }

    public static class DestinationWithFinalMembers {

        private String a;

        public final String getA() {
            return a;
        }

        public final void setA(String a) {
            this.a = a;
        }
    }

    public static final class FinalSource {

        private String x;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    public final static class FinalDestination {

        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static class PrivateDefaultConstructorSource {

        private PrivateDefaultConstructorSource() {
        }
    }

    public static class PrivateDefaultConstructorDestination {

        private PrivateDefaultConstructorDestination() {
        }
    }

    public class NonStaticSource {
    }

    public class NonStaticDestination {
    }

    public static class InheritedFromSource extends Source {
    }

    public static class InheritedFromDestination extends Destination {
    }

    public static class NoDefaultConstructorSource {

        public NoDefaultConstructorSource(final int x) {
        }
    }

    public static class NoDefaultConstructorDestination {

        public NoDefaultConstructorDestination(final int y) {
        }
    }

    public static class SourceWithWithProtectedDefaultConstructor {

        private String x;

        protected SourceWithWithProtectedDefaultConstructor() {
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    public static class InheritedFromSourceWithWithProtectedDefaultConstructor
            extends SourceWithWithProtectedDefaultConstructor {
    }

    public static class DestinationWithProtectedDefaultConstructor {

        private String a;

        protected DestinationWithProtectedDefaultConstructor() {
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static class InheritedFromDestinationWithProtectedDefaultConstructor
            extends DestinationWithProtectedDefaultConstructor {
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
    public void mapper_should_accept_inherited_classes() {
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

    @Test
    public void mapper_should_accept_final_source_classes()
            throws NoSuchFieldException {
        // GIVEN
        FinalSource sampleSource = new FinalSource();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(FinalSource.class, Destination.class,
                        (config, ref) -> config
                        .bindFunction(ref.source()::getX, ref.destination()::setA))
                .buildMapper();

        Destination result = new Destination();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
    }

    @Test
    public void mapper_should_accept_classes_with_protected_default_constructor()
            throws NoSuchFieldException {
        // GIVEN
        Source sampleSource = new Source();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, DestinationWithProtectedDefaultConstructor.class,
                        (config, ref) -> config
                        .bindFunction(ref.source()::getX, ref.destination()::setA))
                .buildMapper();

        DestinationWithProtectedDefaultConstructor result
                = new InheritedFromDestinationWithProtectedDefaultConstructor();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_final_destination_classes()
            throws NoSuchFieldException {
        // GIVEN
        Source sampleSource = new Source();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, FinalDestination.class,
                        (config, ref) -> config
                        .bindFunction(ref.source()::getX, ref.destination()::setA))
                .buildMapper();

        // THEN: exception expected
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_source_classes_with_private_default_constructor()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(PrivateDefaultConstructorSource.class, Destination.class,
                        (config, ref) -> {
                        });
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_destination_classes_with_private_default_constructor()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(Source.class, PrivateDefaultConstructorDestination.class,
                        (config, ref) -> {
                        });
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_non_static_classes()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(NonStaticSource.class, NonStaticDestination.class,
                        (config, ref) -> {
                        });
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_source_classes_with_no_default_constructor()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(NoDefaultConstructorSource.class, Destination.class,
                        (config, ref) -> {
                        });
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_destination_classes_with_no_default_constructor()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(Source.class, NoDefaultConstructorDestination.class,
                        (config, ref) -> {
                        });
    }

}
