package org.stjs.generator.lib.Math;

import org.stjs.generator.utils.TestUtils;

public class Math1_min {

    public static boolean main(String[] args) {
        TestUtils.assertEquals(1, Math.min(1, 2), "int");
        TestUtils.assertEquals(1, Math.min(2, 1), "int");

        TestUtils.assertEquals(1l, Math.min(1l, 2l), "long");
        TestUtils.assertEquals(1l, Math.min(2l, 1l), "long");

        TestUtils.assertEquals(1f, Math.min(1f, 2f), "float");
        TestUtils.assertEquals(1f, Math.min(2f, 1f), "float");

        TestUtils.assertEquals(1d, Math.min(1d, 2d), "double");
        TestUtils.assertEquals(1d, Math.min(2d, 1d), "double");

        return true;
    }

}
