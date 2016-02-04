package org.stjs.generator.writer.inlineObjects;

public class InlineObjects11e_AnonymousClass_2LevelDepth_using_variables_from_outerClass {

    public static class BaseClass {
        protected String fieldDefinedInOuterBaseClass = "fieldDefinedInOuterBaseClass";
    }

    public static class SubClass extends BaseClass {
        private String fieldDefinedInOuterClass = "fieldDefinedInOuterClass";

        public String doIt() {
            final String variableDefinedInMethod = "variableDefinedInMethod";

            // AnonymousClass: Level 1
            return new Dummy() {
                String variableDefinedInLevel1AnonymousClass = "variableDefinedInLevel1AnonymousClass";

                @Override
                public String doIt() {

                    // AnonymousClass: Level 2
                    return new Dummy() {
                        String variableDefinedInLevel2AnonymousClass = "variableDefinedInLevel2AnonymousClass";

                        @Override
                        public String doIt() {
                            return variableDefinedInLevel2AnonymousClass + "-" + variableDefinedInLevel1AnonymousClass + "-" + variableDefinedInMethod + "-" + fieldDefinedInOuterClass + "-" + fieldDefinedInOuterBaseClass;
                        }

                    }.doIt();
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
