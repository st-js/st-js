package org.stjs.generator.writer.trycatch;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class TryCatch3_multipleCatchClauses_NotExpectedException {

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

    public static class CustomExceptionC extends RuntimeException {
        public CustomExceptionC(String message) {
            super(message);
        }
    }

    public static String main(String[] args) {
        Array<Object> array = JSCollections.$array();
        try {
            throw new CustomExceptionC("A RuntimeException for test purpose... this should not be catched!!");
        } catch (CustomExceptionA customA) {
            array.push("CustomExceptionA: " + customA.getMessage());
        } catch (CustomExceptionB customb) {
            array.push("CustomExceptionB: " + customb.getMessage());
        }

        return array.join();
    }


}
