package org.stjs.generator.writer.trycatch;

import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

public class TryCatch5_multipleExceptionTypesOnCatchClause {

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
            throw new CustomExceptionA("A RuntimeException for test purpose... this should be catched!!");
        } catch (CustomExceptionA | CustomExceptionB e) {
            array.push("CustomExceptionA or CustomExceptionB: " + e.getMessage());
        } catch (CustomExceptionC e) {
            array.push("CustomExceptionC: " + e.getMessage());
        }

        return array.join();
    }


}
