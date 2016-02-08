package org.stjs.generator.lib.Math;

import org.stjs.generator.utils.TestUtils;

public class Math3_abs {

    public static boolean main(String[] args) {
        TestUtils.assertEquals(1, Math.abs(1), "int");
        TestUtils.assertEquals(1, Math.abs(-1), "int");

        TestUtils.assertEquals(1l, Math.abs(1l), "long");
        TestUtils.assertEquals(1l, Math.abs(-1l), "long");

        TestUtils.assertEquals(1f, Math.abs(1f), "float");
        TestUtils.assertEquals(1f, Math.abs(-1f), "float");

        TestUtils.assertEquals(1d, Math.abs(1d), "double");
        TestUtils.assertEquals(1d, Math.abs(-1d), "double");

        return true;
    }

}
