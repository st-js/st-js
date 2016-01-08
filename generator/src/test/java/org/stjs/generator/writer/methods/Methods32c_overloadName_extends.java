package org.stjs.generator.writer.methods;

public class Methods32c_overloadName_extends {

    public class SpecificType<T extends String> extends AbstractClass<T> {
        @Override
        protected void aMethod(T item) {
        }
    }

    public abstract class AbstractClass<T> {
        protected void aMethod(T item) {
        }
    }
}