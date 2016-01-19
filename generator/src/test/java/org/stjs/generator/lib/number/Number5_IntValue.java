package org.stjs.generator.lib.number;

public class Number5_IntValue {
	public static boolean main(String[] args) {
		Number number = Integer.valueOf(123);
		assertEquals(123, number.intValue(), "intValue");
		assertEquals(123, number.longValue(), "longValue");
		assertEquals(123, number.shortValue(), "shortValue");
		assertEquals(123, number.byteValue(), "byteValue");
		assertEquals(123, number.floatValue(), "floatValue");
		assertEquals(123, number.doubleValue(), "doubleValue");

		return true;
	}

	private static void assertEquals(Object expected, Object actual, String reference) {
		if (!expected.equals(actual)) {
			throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
		}
	}

}
