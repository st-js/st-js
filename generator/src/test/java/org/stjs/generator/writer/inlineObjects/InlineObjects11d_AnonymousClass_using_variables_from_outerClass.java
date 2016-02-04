package org.stjs.generator.writer.inlineObjects;

public class InlineObjects11d_AnonymousClass_using_variables_from_outerClass {

    public static class BaseClass {
        protected String fieldDefinedInOuterBaseClass = "fieldDefinedInOuterBaseClass";
    }

    public static class SubClass extends BaseClass {
        private String fieldDefinedInOuterClass = "fieldDefinedInOuterClass";

        public String doIt() {
            final String variableDefinedInMethod = "variableDefinedInMethod";
            return new Dummy() {
                @Override
                public String doIt() {
                    return variableDefinedInMethod + "-" + fieldDefinedInOuterClass + "-" + fieldDefinedInOuterBaseClass;
                }
            }.doIt();
        }

    }


    private interface Dummy {
        String doIt();
    }

    public static String main(String[] args) {
        return new SubClass().doIt();
    }

}
