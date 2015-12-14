package org.stjs.generator.lib.string;

public class String14_overloaded_methods {
    public static void main(String[] args) {
        String test = "abcd";

        test.regionMatches(false, 1, "bc", 0, 2);
        test.regionMatches(1, "bc", 0, 2);

        test.indexOf("ab");
        test.indexOf("ab", 0);
        test.indexOf(1);
        test.indexOf(1, 0);
    }
}
