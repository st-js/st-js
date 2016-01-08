package org.stjs.generator.writer.methods;

public class Methods32c_overloadName_extends_baseclass_with_typed_vars_and_override_concrete_method {

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
