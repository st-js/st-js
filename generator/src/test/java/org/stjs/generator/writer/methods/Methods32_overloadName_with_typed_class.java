package org.stjs.generator.writer.methods;

public class Methods32_overloadName_with_typed_class {
    public Methods32_overloadName_with_typed_class() {
    }

    public static class GenericInterface<T> {
        public void aMethod(T t) {
        }
    }

    public static class SubClass extends GenericInterface<String> {
        @Override
        public void aMethod(String s) {
        }
    }
}

