package org.stjs.generator.lib.number;

import org.stjs.generator.utils.TestUtils;

public class Number6_toString {

    public static boolean main(String[] args) {
        testIntegerToString_withInt();
        testIntegerToString_withInt_Radix();
        return true;
    }

    private static void testIntegerToString_withInt() {
        TestUtils.assertEquals("0", Integer.toString(0), "Integer.toString(0)");
        TestUtils.assertEquals("1", Integer.toString(1), "Integer.toString(1)");
        TestUtils.assertEquals("10", Integer.toString(10), "Integer.toString(10)");
        TestUtils.assertEquals("-1", Integer.toString(-1), "Integer.toString(-1)");
    }

    private static void testIntegerToString_withInt_Radix() {

        TestUtils.assertEquals("0", Integer.toString(0, 10), "Integer.toString(0, radix_10)");
        TestUtils.assertEquals("1", Integer.toString(1, 10), "Integer.toString(1, radix_10)");
        TestUtils.assertEquals("10", Integer.toString(10, 10), "Integer.toString(10, radix_10)");
        TestUtils.assertEquals("-1", Integer.toString(-1, 10), "Integer.toString(-1, radix_10)");

        TestUtils.assertEquals("0", Integer.toString(0, 8), "Integer.toString(0, radix_8)");
        TestUtils.assertEquals("1", Integer.toString(1, 8), "Integer.toString(1, radix_8)");
        TestUtils.assertEquals("12", Integer.toString(10, 8), "Integer.toString(10, radix_8)");
        TestUtils.assertEquals("-1", Integer.toString(-1, 8), "Integer.toString(-1, radix_8)");

        TestUtils.assertEquals("0", Integer.toString(0, 16), "Integer.toString(0, radix_16)");
        TestUtils.assertEquals("1", Integer.toString(1, 16), "Integer.toString(1, radix_16)");
        TestUtils.assertEquals("a", Integer.toString(10, 16), "Integer.toString(10, radix_16)");
        TestUtils.assertEquals("f", Integer.toString(15, 16), "Integer.toString(15, radix_16)");
        TestUtils.assertEquals("10", Integer.toString(16, 16), "Integer.toString(16, radix_16)");
        TestUtils.assertEquals("14", Integer.toString(20, 16), "Integer.toString(20, radix_16)");
        TestUtils.assertEquals("-1", Integer.toString(-1, 16), "Integer.toString(-1, radix_16)");
    }

}
