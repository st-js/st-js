package org.stjs.generator.writer.inlineObjects;

public class InlineObjects11f_AnonymousClass_DeclaredAsField_using_variables_from_outerClass {

    private String fieldDefinedInOuterClass = "fieldDefinedInOuterClass";

    private interface Dummy {
        String doIt();
    }

    private Dummy myDummy = new Dummy() {
        @Override
        public String doIt() {
            return fieldDefinedInOuterClass;
        }
    };

    public String execute() {
        return myDummy.doIt();
    }

    public static String main(String[] args) {
        return new InlineObjects11f_AnonymousClass_DeclaredAsField_using_variables_from_outerClass().execute();
    }

}
