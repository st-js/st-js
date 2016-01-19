package org.stjs.generator.lib.number;

public class Number3_NumberValueOf {
	public static boolean main(String[] args) {
		assertEquals(123, Integer.valueOf("123"), "Integer.valueOf(\"123\")");
		assertEquals(123, Integer.valueOf(123), "Integer.valueOf(123)");

		assertEquals(123L, Long.valueOf("123"), "Long.valueOf(\"123\")");
		assertEquals(123L, Long.valueOf(123), "Long.valueOf(123)");
		return true;
	}

	private static void assertEquals(Object expected, Object actual, String reference) {
		if (!expected.equals(actual)) {
			throw new RuntimeException("Assertion error for '" + reference + "': " + expected + " != " + actual);
		}
	}

}
