package org.stjs.generator.lib.string;

public class String17_Substring {

    public static boolean main(String[] args) {
        // Start and end index
        assertEquals("abcd", "abcd".substring(0, 4), "Whole string");
        assertEquals("a", "abcd".substring(0, 1), "First char");
        assertEquals("ab", "abcd".substring(0, 2), "Starting 2 chars");
        assertEquals("b", "abcd".substring(1, 2), "Second chars");
        assertEquals("d", "abcd".substring(3, 4), "Last char");
        assertEquals("", "abcd".substring(1, 1), "No char");

        // Start index only
        assertEquals("abcd", "abcd".substring(0), "Starting at 0");
        assertEquals("bcd", "abcd".substring(1), "Starting at 1");
        assertEquals("cd", "abcd".substring(2), "Starting at 2");
        assertEquals("d", "abcd".substring(3), "Starting at 3");
        assertEquals("", "abcd".substring(4), "Starting at 4");

        return true;
    }

    private static void assertEquals(String expected, String actual, String reference) {
        if (actual != expected) {
            throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
        }
    }

}
