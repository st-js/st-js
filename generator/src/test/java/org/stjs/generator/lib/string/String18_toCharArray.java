package org.stjs.generator.lib.string;

public class String18_toCharArray {

    public static boolean main(String[] args) {
        char[] charArray = "abcd".toCharArray();
        assertEquals(4, charArray.length, "length == 0");
        assertEquals("a", charArray[0], "idx 0");
        assertEquals("b", charArray[1], "idx 1");
        assertEquals("c", charArray[2], "idx 2");
        assertEquals("d", charArray[3], "idx 3");

        charArray = "".toCharArray();
        assertEquals(0, charArray.length, "length == 0");

        return true;
    }

    private static void assertEquals(Object expected, Object actual, String reference) {
        if (actual != expected) {
            throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
        }
    }

}
