package org.stjs.generator.writer.inlineObjects;

public class InlineObjects11_AnonymousClass_calling_outer {

    public String doIt() {
        return new Dummy() {
            @Override
            public String doIt() {
                return "doIt()_Dummy.doIt()_outerMethod-" + outerMethod();
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
                        // call outer class
                        return "doIt2()_SuperDummy.doIt()_Dummy.doIt()_outerMethod-" + outerMethod() + "_superDoIt-" + superDoIt();
                    }
                }.doIt();
            }

            @Override
            public String superDoIt() {
                return "inSuperDoIt_outerMethod-" + outerMethod();
            }
        }.doIt();
    }

    public String outerMethod() {
        return "InsideOuterMethod!";
    }

    private interface Dummy {
        String doIt();
    }

    private interface SuperDummy {
        String doIt();
        String superDoIt();
    }

    public static String main(String[] args) {
        return
                "#1- " + new InlineObjects11_AnonymousClass_calling_outer().doIt() + "\n" +
                "#2- " + new InlineObjects11_AnonymousClass_calling_outer().doIt2();
    }

}
