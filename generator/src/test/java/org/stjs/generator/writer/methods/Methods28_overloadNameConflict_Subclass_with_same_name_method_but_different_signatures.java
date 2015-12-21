package org.stjs.generator.writer.methods;

public class Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures {
    public Methods28_overloadNameConflict_Subclass_with_same_name_method_but_different_signatures() {
    }

    public static class BaseClass {
        public void aMethod1(int i) {
        }

        public void aMethod2(int i) {
        }
    }

    public static class SubClass extends BaseClass {
        public void aMethod1(String s) {
        }

        public void aMethod2(int i) {
        }

        public void aMethod2(String s) {
        }

    }
}

