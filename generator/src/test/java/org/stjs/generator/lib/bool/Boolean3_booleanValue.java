package org.stjs.generator.lib.bool;

import org.stjs.javascript.JSObjectAdapter;

public class Boolean3_booleanValue {

    public static boolean main(String[] args)
    {
        assertEquals(true, Boolean.TRUE.booleanValue(), "Boolean.TRUE.booleanValue()");
        assertEquals(false, Boolean.FALSE.booleanValue(), "Boolean.FALSE.booleanValue()");

        return true;
    }

    private static void assertEquals(Object expected, Object actual, String reference) {
        boolean areEquals = false;
        JSObjectAdapter.$js("areEquals = (expected === actual)");
        if (!areEquals) {
            throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
        }
    }

}
