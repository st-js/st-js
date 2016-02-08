package org.stjs.generator.lib.number;

import org.stjs.generator.utils.TestUtils;

public class Number3_NumberValueOf {
	public static boolean main(String[] args) {
		testIntegerFromString_valueOfString();
		testIntegerFromString_valueOfString_Radix();
		return true;
	}

	private static void testIntegerFromString_valueOfString() {
		TestUtils.assertEquals(0, Integer.valueOf("0").intValue(), "Integer.valueOf(0)");
		TestUtils.assertEquals(1, Integer.valueOf("1").intValue(), "Integer.valueOf(1)");
		TestUtils.assertEquals(10, Integer.valueOf("10").intValue(), "Integer.valueOf(10)");
		TestUtils.assertEquals(-1, Integer.valueOf("-1").intValue(), "Integer.valueOf(-1)");
	}

	private static void testIntegerFromString_valueOfString_Radix() {
		TestUtils.assertEquals(0, Integer.valueOf("0", 10).intValue(), "Integer.valueOf(0, radix_10)");
		TestUtils.assertEquals(1, Integer.valueOf("1", 10).intValue(), "Integer.valueOf(1, radix_10)");
		TestUtils.assertEquals(10, Integer.valueOf("10", 10).intValue(), "Integer.valueOf(10, radix_10)");
		TestUtils.assertEquals(-1, Integer.valueOf("-1", 10).intValue(), "Integer.valueOf(-1, radix_10)");

		TestUtils.assertEquals(0, Integer.valueOf("0", 8).intValue(), "Integer.valueOf(0, radix_8)");
		TestUtils.assertEquals(1, Integer.valueOf("1", 8).intValue(), "Integer.valueOf(1, radix_8)");
		TestUtils.assertEquals(7, Integer.valueOf("7", 8).intValue(), "Integer.valueOf(7, radix_8)");
		TestUtils.assertEquals(8, Integer.valueOf("10", 8).intValue(), "Integer.valueOf(10, radix_8)");
		TestUtils.assertEquals(9, Integer.valueOf("11", 8).intValue(), "Integer.valueOf(12, radix_8)");
		TestUtils.assertEquals(-1, Integer.valueOf("-1", 8).intValue(), "Integer.valueOf(-1, radix_8)");

		TestUtils.assertEquals(0, Integer.valueOf("0", 16).intValue(), "Integer.valueOf(0, radix_16)");
		TestUtils.assertEquals(1, Integer.valueOf("1", 16).intValue(), "Integer.valueOf(1, radix_16)");
		TestUtils.assertEquals(10, Integer.valueOf("a", 16).intValue(), "Integer.valueOf(10, radix_16)");
		TestUtils.assertEquals(15, Integer.valueOf("f", 16).intValue(), "Integer.valueOf(15, radix_16)");
		TestUtils.assertEquals(16, Integer.valueOf("10", 16).intValue(), "Integer.valueOf(16, radix_16)");
		TestUtils.assertEquals(20, Integer.valueOf("14", 16).intValue(), "Integer.valueOf(20, radix_16)");
		TestUtils.assertEquals(-1, Integer.valueOf("-1", 16).intValue(), "Integer.valueOf(-1, radix_16)");
	}


}
