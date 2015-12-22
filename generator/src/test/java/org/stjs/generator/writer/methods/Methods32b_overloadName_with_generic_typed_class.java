package org.stjs.generator.writer.methods;

public class Methods32b_overloadName_with_generic_typed_class {

    public static class GenericInterface<T> {
        public void aMethod(T t) {
        }
    }

    public static class SubClassGeneric<T> extends GenericInterface<T> {
        @Override
        public void aMethod(T t) {
        }
    }

}

