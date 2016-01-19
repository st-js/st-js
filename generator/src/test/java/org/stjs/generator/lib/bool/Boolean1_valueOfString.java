package org.stjs.generator.lib.bool;

import org.stjs.javascript.JSObjectAdapter;

public class Boolean1_valueOfString {

    public static boolean main(String[] args) {
        assertSameInstance(Boolean.TRUE, Boolean.valueOf("true"), "Boolean.valueOf(\"true\")");
        assertSameInstance(Boolean.TRUE, Boolean.valueOf("TRuE"), "Boolean.valueOf(\"TRuE\")");
        assertSameInstance(Boolean.FALSE, Boolean.valueOf("false"), "Boolean.valueOf(\"false\")");
        assertSameInstance(Boolean.FALSE, Boolean.valueOf("patate"), "Boolean.valueOf(\"patate\")");
        assertSameInstance(Boolean.FALSE, Boolean.valueOf(null), "Boolean.valueOf(null)");

        return true;
    }

    private static void assertSameInstance(Object expected, Object actual, String reference) {
        boolean areEquals = false;
        JSObjectAdapter.$js("areEquals = (expected === actual)");
        if (!areEquals) {
            throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
        }
    }

}
