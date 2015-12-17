package org.stjs.generator.writer.overload;

public class Overload9_no_overload_base_class {
    public Overload9_no_overload_base_class() {
        BaseClass baseClass = new ExtendedClass();
        baseClass.overloadMethod(2);
    }

    public class BaseClass {
        public void overloadMethod(int param) {
        }
    }

    public class ExtendedClass extends BaseClass {
        public void overloadMethod(int param) {
        }

        public void overloadMethod(String param) {
        }
    }
}

