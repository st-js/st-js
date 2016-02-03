package org.stjs.generator.writer.innerTypes;

public class InnerTypes28_acces_outterClass_field_member_from_inner_class {

    private String fieldInOuterType;

    public class InnerClass {
        public InnerClass() {
            fieldInOuterType = "assigning it from the constructor";
        }

        public void aMethod() {
            fieldInOuterType = "assigning it from a standard method";
        }
    }

}
