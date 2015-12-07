package org.stjs.generator.writer.inlineObjects;

public class InlineObjects11_AnonymousClass_calling_outer {

    public String doIt() {
        return new Dummy() {
            @Override
            public String doIt() {
                return outerMethod();
            }
        }.doIt();
    }

    public String doIt2() {

        return new SuperDummy() {
            @Override
            public String doIt() {
                return new Dummy() {
                    @Override
                    public String doIt() {
                        return outerMethod() + superDoIt();
                    }
                }.doIt();
            }

            @Override
            public String superDoIt() {
                return "superDoIt() --> " + outerMethod();
            }
        }.doIt();
    }

    public String outerMethod() {
        return "InlineObjects11_AnonymousClass_calling_outer.outerMethod()";
    }

    private interface Dummy {
        String doIt();
    }

    private interface SuperDummy {
        String doIt();
        String superDoIt();
    }

}