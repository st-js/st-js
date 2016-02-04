package org.stjs.generator.lib.javaclass;

import org.stjs.generator.utils.TestUtils;

public class JavaClassTest5_Class_isAssignableFrom {

    private interface BaseInterface {
    }

    private interface ImplementedInterface extends BaseInterface {
    }

    private static class BaseClass {
    }

    private static class SubClass extends BaseClass implements ImplementedInterface {
    }

    private interface DummyInterface {
    }

    private interface DummyClass {
    }

    public static boolean main(String[] args) {
        TestUtils.assertEquals(true, SubClass.class.isAssignableFrom(SubClass.class), "Class assignable from itself");
        TestUtils.assertEquals(true, BaseClass.class.isAssignableFrom(SubClass.class), "BaseClass class assignable from SubClass");
        TestUtils.assertEquals(true, ImplementedInterface.class.isAssignableFrom(SubClass.class), "ImplementedInterface assignable from SubClass");
        TestUtils.assertEquals(true, BaseInterface.class.isAssignableFrom(SubClass.class), "BaseInterface assignable from SubClass");

        TestUtils.assertEquals(false, SubClass.class.isAssignableFrom(DummyInterface.class), "SubClass NOT assignable from DummyInterface");
        TestUtils.assertEquals(false, SubClass.class.isAssignableFrom(DummyClass.class), "SubClass NOT assignable from DummyClass");

        return true;
    }

}
