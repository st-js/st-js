package org.stjs.generator.lib.string;

import org.stjs.generator.utils.TestUtils;

public class String21_stringFormat {

    public static Boolean main(String[] args) {
        TestUtils.assertEquals("abc", String.format("abc"), "abc");
        TestUtils.assertEquals("abc A", String.format("abc %s", "A"), "abc A");
        TestUtils.assertEquals("abc A B", String.format("abc %s %s", "A", "B"), "abc A B");

        TestUtils.assertEquals("abc 1", String.format("abc %d", 1), "abc 1");
        TestUtils.assertEquals("abc 1 A", String.format("abc %d %s", 1, "A"), "abc 1 A");

        TestUtils.assertEquals("abc A 1", String.format("abc %2$s %1$d", 1, "A"), "abc A 1");

        return true;
    }

}
