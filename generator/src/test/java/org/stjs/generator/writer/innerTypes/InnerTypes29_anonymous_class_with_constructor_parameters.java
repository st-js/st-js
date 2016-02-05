package org.stjs.generator.writer.innerTypes;

public class InnerTypes29_anonymous_class_with_constructor_parameters {

    public abstract class InnerClassWithParameters {

        protected String value;

        public InnerClassWithParameters(String value) {
            this.value = value;
        }

        public abstract String getValue();
    }

    private InnerClassWithParameters createInnerInstance() {
        return new InnerClassWithParameters("This is the value that has been passed in the constructor") {
            @Override
            public String getValue() {
                return value;
            }
        };
    }

    public static String main(String[] args) {
        InnerTypes29_anonymous_class_with_constructor_parameters obj = new InnerTypes29_anonymous_class_with_constructor_parameters();
        InnerClassWithParameters innerInstance = obj.createInnerInstance();
        return innerInstance.getValue();
    }

}
