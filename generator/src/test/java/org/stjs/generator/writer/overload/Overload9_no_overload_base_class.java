package org.stjs.generator.writer.overload;

public class Overload9_no_overload_base_class {
    public Overload9_no_overload_base_class() {
        BaseClass baseClass = new ExtendedClass();
        baseClass.overloadMethod();
    }

    public class BaseClass {
        public void overloadMethod() {
        }
    }

    public class ExtendedClass extends BaseClass {
        public void overloadMethod() {
        }

        public void overloadMethod(String param) {
        }
    }
}

