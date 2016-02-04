package org.stjs.generator.writer.inheritance;

public class Inheritance9_private_constructor {

    public static class ClassWithPrivateConstructor {

        private final String value;

        private ClassWithPrivateConstructor(String value) {
            this.value = value;
        }

        public static ClassWithPrivateConstructor createNewInstance(String value) {
            return new ClassWithPrivateConstructor(value);
        }
    }

    public static String main(String[] args) {
        ClassWithPrivateConstructor newInstance = ClassWithPrivateConstructor.createNewInstance("Value passed as parameters on the static constructor method");
        return newInstance.value;
    }

}
