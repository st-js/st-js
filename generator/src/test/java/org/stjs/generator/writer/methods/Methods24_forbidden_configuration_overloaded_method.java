package org.stjs.generator.writer.methods;

public class Methods24_forbidden_configuration_overloaded_method {

    public void main(String[] args) {
        forbiddenMethod("test", 0);
    }

    public void forbiddenMethod() {
    }

    public void forbiddenMethod(String testString) {
        forbiddenMethod();
    }

    public void forbiddenMethod(String testString, int testInt) {
        forbiddenMethod(testString);
    }
}
