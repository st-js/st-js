package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;

public class HashCode7_javaLangClass_Equals {

    public static class ClassA {
    }

    public static class ClassB {
    }

    public static boolean main(String[] args) {
        Object objA = new ClassA();
        Object objB = new ClassB();

        TestUtils.assertEquals(objA.getClass(), objA.getClass(), "objA.getClass().equals --> objA.getClass()");
        TestUtils.assertEquals(objA.getClass(), ClassA.class, "objA.getClass().equals --> ClassA.class");

        TestUtils.assertEquals(objB.getClass(), objB.getClass(), "objB.getClass().equals --> objB.getClass()");
        TestUtils.assertEquals(objB.getClass(), ClassB.class, "objB.getClass().equals --> ClassB.class");

        TestUtils.assertNotEquals(objA.getClass(), objB.getClass(), "objA.hashCode() !== objB.hashCode()");

        return true;
    }

}
