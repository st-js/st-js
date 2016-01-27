package org.stjs.generator.lib.exception;

public class Exception1_ExceptionGetMessage {

    public static String main(String[] args) {
        RuntimeException exception = new RuntimeException("This is the exception message");
        return exception.getMessage();
    }

}
