package org.stjs.generator.writer.methods;

import java.util.List;

public class Methods32d_overloadName_override_method_declaring_typedvar_type {

    public interface TheInterface {
        <T> void add(List<T> list, T value);
    }

    public class TheClass implements TheInterface {
        @Override
        public <T> void add(List<T> list, T value) {
            //
        }
    }

}
