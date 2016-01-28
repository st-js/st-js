package org.stjs.generator.lib.hashcode;

import org.stjs.generator.utils.TestUtils;
import org.stjs.javascript.JSObjectAdapter;

public class HashCode2_strings {

    public static boolean main(String[] args) {
        TestUtils.assertEquals(true, TestUtils.areSameInstance(hashCodeForString("ABC"), hashCodeForString("ABC")), "ABC.hashCode() === ABC.hashCode()");
        TestUtils.assertEquals(false, TestUtils.areSameInstance(hashCodeForString("ABC"), hashCodeForString("DEF")), "ABC.hashCode() !== DEF.hashCode()");
        return true;
    }

    private static Object hashCodeForString(String theStr) {
        Object hashCode = null;
        JSObjectAdapter.$js("hashCode = theStr.$java_hashCode();");
        return hashCode;
    }

}
