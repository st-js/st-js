package org.stjs.generator.writer.fields;

public class Fields31_protected_parent {
    protected String getProtectedField() {
        return "test";
    }

    private static class InnerClass {
        private Fields31_protected_parent parent;

        public InnerClass(Fields31_protected_parent parent) {
            this.parent = parent;
        }

        public String getProtectedParentMethod() {
            return parent.getProtectedField();
        }
    }
}
