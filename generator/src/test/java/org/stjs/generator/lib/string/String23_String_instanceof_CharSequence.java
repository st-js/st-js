package org.stjs.generator.lib.string;

import org.stjs.generator.utils.TestUtils;

public class String23_String_instanceof_CharSequence {

    public static Boolean main(String[] args) {
        CharSequence theStringAsCharSequence = (CharSequence)"ABCDEFGHIJ";
        TestUtils.assertEquals(true, theStringAsCharSequence instanceof CharSequence, "theStringAsCharSequence instanceof CharSequence");
        return true;
    }

}
