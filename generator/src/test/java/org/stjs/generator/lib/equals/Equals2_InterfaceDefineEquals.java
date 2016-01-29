package org.stjs.generator.lib.equals;

import org.stjs.generator.utils.TestUtils;

public class Equals2_InterfaceDefineEquals {

    public interface MyInterface {
        boolean equals(Object o);
    }

    public static class MyClass implements MyInterface {
        private final String value;

        public MyClass(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            return value == ((MyClass)o).value;
        }
    }

    public static boolean main(String[] args) {
        Object objA1 = new MyClass("A");
        Object objA2 = new MyClass("A");
        Object objB = new MyClass("B");

        TestUtils.assertEquals(objA1, objA1, "objA1.equals --> objA1");
        TestUtils.assertEquals(objA1, objA2, "objA1.equals --> objA2");
        TestUtils.assertEquals(objB, objB, "objB.equals --> objB");
        TestUtils.assertNotEquals(objA1, objB, "!objA.equals(objB)");

        return true;
    }

}
