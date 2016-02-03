package org.stjs.generator.lib.string;

import org.stjs.generator.utils.TestUtils;

public class String24_String_split {

    public static Boolean main(String[] args) {
        TestUtils.assertEquals(new String[]{""}, "".split("\\."), "");
        TestUtils.assertEquals(new String[]{"ABCDEFGHIJ"}, "ABCDEFGHIJ".split("Z"), "");

        TestUtils.assertEquals(new String[]{"", "A", "B", "C"}, "ABC".split(""), "");

        TestUtils.assertEquals(new String[]{"A", "B", "C"}, "A-B-C".split("-"), "");
        TestUtils.assertEquals(new String[]{"A", "B", ""}, "A-B-".split("-"), "");

        TestUtils.assertEquals(new String[]{"A", "B", "C"}, "A.B.C".split("\\."), "");

        return true;
    }

}
