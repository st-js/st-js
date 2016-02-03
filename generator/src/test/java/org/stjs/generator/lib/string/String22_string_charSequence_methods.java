package org.stjs.generator.lib.string;

import org.stjs.generator.utils.TestUtils;

public class String22_string_charSequence_methods {

    public static Boolean main(String[] args) {
        CharSequence theStringAsCharSequence = (CharSequence)"ABCDEFGHIJ";

        TestUtils.assertEquals("ABCDEFGHIJ", theStringAsCharSequence.toString(), "theStringAsCharSequence.toString()");
        TestUtils.assertEquals(10, theStringAsCharSequence.length(), "theStringAsCharSequence.length()");
        TestUtils.assertEquals('A', theStringAsCharSequence.charAt(0), "theStringAsCharSequence.charAt(0)");
        TestUtils.assertEquals('B', theStringAsCharSequence.charAt(1), "theStringAsCharSequence.charAt(1)");
        TestUtils.assertEquals('J', theStringAsCharSequence.charAt(9), "theStringAsCharSequence.charAt(9)");

        TestUtils.assertEquals("A", theStringAsCharSequence.subSequence(0, 1), "theStringAsCharSequence.subSequence(0, 1)");
        TestUtils.assertEquals("AB", theStringAsCharSequence.subSequence(0, 2), "theStringAsCharSequence.subSequence(0, 2)");
        TestUtils.assertEquals("B", theStringAsCharSequence.subSequence(1, 2), "theStringAsCharSequence.subSequence(1, 2)");
        TestUtils.assertEquals("ABCDEFGHIJ", theStringAsCharSequence.subSequence(0, 10), "theStringAsCharSequence.subSequence(0, 10)");

        return true;
    }

}
