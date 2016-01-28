package org.stjs.generator.utils;

import org.stjs.javascript.JSObjectAdapter;

public class TestUtils {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return joinCharArray(hexChars);
    }

    public static String joinCharArray(char[] charArray) {
        String result = "";
        for (char c : charArray) {
            result = result + c;
        }

        return result;
    }

    public static void assertEquals(Object expectedValue, Object currentValue, String message) {
        if (expectedValue == currentValue) {
            return;
        }

        if ((expectedValue == null) || (!expectedValue.equals(currentValue))) {
            String errorMessage = "Expecting [" + nullSafeToString(expectedValue) + "], Current [" + nullSafeToString(currentValue) + "]: " + message;
            JSObjectAdapter.$js("throw new Error(errorMessage)");
        }
    }

    public static void assertEquals(int expectedValue, int currentValue, String message) {
        if (expectedValue == currentValue) {
            return;
        } else {
            String errorMessage = "Expecting [" + nullSafeToString(expectedValue) + "], Current [" + nullSafeToString(currentValue) + "]: " + message;
            JSObjectAdapter.$js("throw new Error(errorMessage)");
        }
    }

    public static String nullSafeToString(Object o) {
        if (o == null) {
            return "null";
        } else {
            return o.toString();
        }
    }

    public static void fail(String message) {
        JSObjectAdapter.$js("throw new Error(message)");
    }

    public static void assertSameInstance(Object obj1, Object obj2, String message) {
        if (!areSameInstance(obj1, obj2)) {
            fail("Assertion error for '" + message + "': " + obj1 + " != " + obj2);
        }
    }

    public static boolean areSameInstance(Object obj1, Object obj2) {
        boolean areEquals = false;
        JSObjectAdapter.$js("areEquals = (obj1 === obj2)");
        return areEquals;
    }

}
