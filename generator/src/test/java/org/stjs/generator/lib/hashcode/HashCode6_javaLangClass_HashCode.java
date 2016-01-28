package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;

public class HashCode6_javaLangClass_HashCode {

    public static class ClassA {
    }

    public static class ClassB {
    }

    public static boolean main(String[] args) {
        Object objA = new ClassA();
        Object objB = new ClassB();

        TestUtils.assertEquals(objA.getClass().hashCode(), objA.getClass().hashCode(), "objA.hashCode() === objA.hashCode()");
        TestUtils.assertEquals(objA.getClass().hashCode(), ClassA.class.hashCode(), "objA.hashCode() === ClassA.class.hashCode()");

        TestUtils.assertEquals(objB.getClass().hashCode(), objB.getClass().hashCode(), "objB.hashCode() === objB.hashCode()");
        TestUtils.assertEquals(objB.getClass().hashCode(), ClassB.class.hashCode(), "objB.hashCode() === ClassB.class.hashCode()");

        TestUtils.assertEquals(true, objA.getClass().hashCode() != objB.getClass().hashCode(), "objA.hashCode() !== objB.hashCode()");

        return true;
    }

}
