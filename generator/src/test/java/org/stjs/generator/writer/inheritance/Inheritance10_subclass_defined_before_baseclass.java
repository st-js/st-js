package org.stjs.generator.writer.inheritance;

public class Inheritance10_subclass_defined_before_baseclass {

    public static class SubClass extends BaseClassDefinedAfterTheSubClass {
    }

    private static class BaseClassDefinedAfterTheSubClass {
    }

    public static boolean main(String[] args) {
        SubClass subClass = new SubClass();
        return true;
    }

}
