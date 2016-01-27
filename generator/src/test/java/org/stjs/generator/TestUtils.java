package org.stjs.generator;

public class TestUtils {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToString(byte[] bytes) {
        String result = "";
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            if (j > 0) {
                result = result + " ";
            }
            int v = bytes[j] & 0xFF;
            result = result + hexArray[v >>> 4];
            result = result + hexArray[v & 0x0F];
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
}
