package org.stjs.generator.writer.trycatch;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class TryCatch2_multipleCatchClauses {

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
            throw new CustomExceptionA("A RuntimeException for test purpose");
        } catch (CustomExceptionA customA) {
            array.push("CustomExceptionA: " + customA.getMessage());
        } catch (CustomExceptionB customb) {
            array.push("CustomExceptionB: " + customb.getMessage());
        }

        return array.join();
    }


}
