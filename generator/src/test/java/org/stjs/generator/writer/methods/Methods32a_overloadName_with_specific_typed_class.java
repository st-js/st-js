package org.stjs.generator.writer.methods;

public class Methods32a_overloadName_with_specific_typed_class {

    public static class GenericInterface<T> {
        public void aMethod(T t) {
        }
    }

    public static class SubClassSpecific extends GenericInterface<String> {
        @Override
        public void aMethod(String s) {
        }
    }


}

