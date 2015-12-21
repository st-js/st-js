package org.stjs.generator.writer.methods;

public class Methods29_overloadNameConflict_2Supertypes_with_same_method_name_but_different_signatures {
    public Methods29_overloadNameConflict_2Supertypes_with_same_method_name_but_different_signatures() {
    }

    public interface Interface1 {
        void aMethod(int i);
    }

    public interface Interface2 {
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

