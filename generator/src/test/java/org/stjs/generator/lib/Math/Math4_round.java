package org.stjs.generator.lib.Math;

import org.stjs.generator.utils.TestUtils;

public class Math4_round {

    public static boolean main(String[] args) {
        TestUtils.assertEquals(1, Math.round(1.0f), "float 1.0");
        TestUtils.assertEquals(1, Math.round(1.1f), "float 1.1");
        TestUtils.assertEquals(2, Math.round(1.5f), "float 1.5");
        TestUtils.assertEquals(2, Math.round(1.999f), "float 1.999");

        TestUtils.assertEquals(1l, Math.round(1.0d), "double 1.0");
        TestUtils.assertEquals(1l, Math.round(1.1d), "double 1.1");
        TestUtils.assertEquals(2l, Math.round(1.5d), "double 1.5");
        TestUtils.assertEquals(2l, Math.round(1.999d), "double 1.999");

        return true;
    }

}
