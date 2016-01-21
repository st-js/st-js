package org.stjs.generator.writer.methods;

public class Methods32e_overloadName_with_specific_typed_class_including_generics<T> {

    public interface GenericInterface<T> {
        int aMethod(T t);
    }

    public interface Dto<T> {
    }

    public GenericInterface<Dto<T>> method() {
        return new GenericInterface<Dto<T>>() {
            @Override
            public int aMethod(Dto<T> dto) {
                return 1234;
            }
        };
    }

}

