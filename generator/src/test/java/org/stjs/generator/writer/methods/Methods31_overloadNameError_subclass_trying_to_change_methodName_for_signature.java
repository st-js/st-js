package org.stjs.generator.writer.methods;

import org.stjs.javascript.annotation.JSOverloadName;

public class Methods31_overloadNameError_subclass_trying_to_change_methodName_for_signature {
    public Methods31_overloadNameError_subclass_trying_to_change_methodName_for_signature() {
    }

    public static class Interface1 {
        public void aMethod(int i) {

        }
    }

    public static class SubClass extends Interface1 {
        @JSOverloadName("aMethodWithInt")
        @Override
        public void aMethod(int i) {

        }
    }
}

