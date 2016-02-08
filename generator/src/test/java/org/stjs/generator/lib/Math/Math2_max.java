package org.stjs.generator.lib.Math;

import org.stjs.generator.utils.TestUtils;

public class Math2_max {

    public static boolean main(String[] args) {
        TestUtils.assertEquals(2, Math.max(1, 2), "int");
        TestUtils.assertEquals(2, Math.max(2, 1), "int");

        TestUtils.assertEquals(2l, Math.max(1l, 2l), "long");
        TestUtils.assertEquals(2l, Math.max(2l, 1l), "long");

        TestUtils.assertEquals(2f, Math.max(1f, 2f), "float");
        TestUtils.assertEquals(2f, Math.max(2f, 1f), "float");

        TestUtils.assertEquals(2d, Math.max(1d, 2d), "double");
        TestUtils.assertEquals(2d, Math.max(2d, 1d), "double");

        return true;
    }

}
