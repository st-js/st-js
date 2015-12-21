package org.stjs.generator.writer.methods;

import org.stjs.javascript.annotation.JSOverloadName;

public class Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation {
    public Methods30_overloadNameConflict_Resolved_by_JSOverloadName_annotation() {
    }

    public interface Interface1 {
        void aMethod(int i);
    }

    public interface Interface2 {
        @JSOverloadName("aMethodWithString")
        void aMethod(String s);
    }

    public static class SubClass implements Interface1, Interface2 {
        @Override
        public void aMethod(int i) {

        }

        @Override
        public void aMethod(String s) {

        }
    }
}

