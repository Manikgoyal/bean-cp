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

public class AfterAndBeforeMapActionTest {

    public static class Source {

        private String x, y;

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

        private String a, b, c;

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

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }

    @Test
    public void before_map_action_should_be_evaluated_if_present() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("abc");
        sourceObject.setY("xyz");

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB)
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'A' property value.", "abc", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "xyz", destinationObject.getB());
        assertEquals("Invalid 'c' property value.", "aorigborig", destinationObject.getC());
    }

    @Test
    public void after_map_action_should_be_evaluated_if_present() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("abc");
        sourceObject.setY("xyz");

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'A' property value.", "abc", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "xyz", destinationObject.getB());
        assertEquals("Invalid 'c' property value.", "abcxyz", destinationObject.getC());
    }

    @Test(expected = MapperConfigurationException.class)
    public void beforeMap_line_must_be_before_binding_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bindConstant("1", destination::setA)
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void afterMap_line_must_be_after_binding_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB));

        // THEN: expect exception
    }
}
