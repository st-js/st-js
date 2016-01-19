package org.stjs.generator.lib.string;

public class String19_replaceWithCharSequence {

    public static boolean main(String[] args) {
        // no replacement
        assertEquals("abcdefghij", "abcdefghij".replace((CharSequence)"!!!!", "#"), "abcdefghij --> abcdefghij");

        // single replacements
        assertEquals("abcde#fghij", "abcde!!fghij".replace((CharSequence)"!!", "#"), "!!abcde!!fghij!! --> #abcde#fghij#");

        // multiple replacements
        assertEquals("#abcde#fghij#", "!!abcde!!fghij!!".replace((CharSequence)"!!", "#"), "!!abcde!!fghij!! --> #abcde#fghij#");

        // no need to escape regex characters
        assertEquals("#abcde#fghij#", ".abcde.fghij.".replace((CharSequence)".", "#"), ".abcde.fghij. --> #abcde#fghij#");

        return true;
    }

    private static void assertEquals(Object expected, Object actual, String reference) {
        if (actual != expected) {
            throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
        }
    }

}
