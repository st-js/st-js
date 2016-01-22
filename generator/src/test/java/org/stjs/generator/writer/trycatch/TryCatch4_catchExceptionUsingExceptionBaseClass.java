package org.stjs.generator.writer.trycatch;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class TryCatch4_catchExceptionUsingExceptionBaseClass {

    public static class CustomExceptionA extends RuntimeException {
        public CustomExceptionA(String message) {
            super(message);
        }
    }

    public static class CustomExceptionB extends RuntimeException {
        public CustomExceptionB(String message) {
            super(message);
        }
    }


    public static String main(String[] args) {
        Array<Object> array = JSCollections.$array();
        try {
            throw new CustomExceptionA("A RuntimeException for test purpose... this should be catched!!");
        } catch (CustomExceptionB customb) {
            array.push("CustomExceptionB: " + customb.getMessage());
        } catch (RuntimeException e) {
            array.push("RuntimeException: " + e.getMessage());
        }

        return array.join();
    }


}
