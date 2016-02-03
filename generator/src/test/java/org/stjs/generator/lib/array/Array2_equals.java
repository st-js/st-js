package org.stjs.generator.lib.array;

import org.stjs.generator.utils.TestUtils;

public class Array2_equals {

    public static boolean main(String[] args) {
        TestUtils.assertEquals(new String[]{}, new String[]{}, "");
        TestUtils.assertEquals(new String[]{"A"}, new String[]{"A"}, "");
        TestUtils.assertEquals(new String[]{"A", "B"}, new String[]{"A", "B"}, "");

        TestUtils.assertNotEquals(new String[]{"A"}, new String[]{"AB"}, "");
        TestUtils.assertNotEquals(new String[]{"A"}, new String[]{"A", "B"}, "");
        TestUtils.assertNotEquals(new String[]{"A"}, null, "");
        TestUtils.assertNotEquals(new String[]{"A"}, "A", "");

        return true;
    };
}
