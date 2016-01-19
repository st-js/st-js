package org.stjs.generator.lib.string;

public class String16_IndexOf {

    public static boolean main(String[] args) {
        // String only
        assertEquals(0, "abcd".indexOf("abcd"), "Exact string match");
        assertEquals(-1, "abcd".indexOf("xyz"), "Not found");
        assertEquals(0, "abcd".indexOf("a"), "First char");
        assertEquals(1, "abcd".indexOf("b"), "Second char");
        assertEquals(3, "abcd".indexOf("d"), "Last char");

        // String and starting index
        assertEquals(0, "abcd".indexOf("abcd", 0), "Exact string match");
        assertEquals(-1, "abcd".indexOf("xyz", 0), "Not found");
        assertEquals(0, "abcd".indexOf("a", 0), "First char");
        assertEquals(1, "abcd".indexOf("b", 0), "Second char");
        assertEquals(3, "abcd".indexOf("d", 0), "Last char");

        assertEquals(1, "aabcd".indexOf("a", 1), "Specific char skipping first one");

        return true;
    }

    private static void assertEquals(int expected, int actual, String reference) {
        if (actual != expected) {
            throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
        }
    }

}
