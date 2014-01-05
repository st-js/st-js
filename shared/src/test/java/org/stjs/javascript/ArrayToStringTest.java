package org.stjs.javascript;

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.Array;

import org.junit.Test;

public class ArrayToStringTest {

	// ====================================================
	// Tests section 15.4.4.2 if the ECMA-262 specification
	// ====================================================

	@Test
	public void testToString01() {
		// The result of calling this function is the same as if
		// the built-in join method were invoked for this object with no argument

		// If the array is empty, return the empty string
		Array<Integer> x = Array();
		assertToStringJoinEquals("", x);
	}

	@Test
	public void testToString02() {
		Array<Integer> x = Array();
		x.$set(0, 1);
		x.$length(0);
		assertToStringJoinEquals("", x);
	}

	@Test
	public void testToString03() {
		// The elements of the array are converted to strings, and these strings are
		// then concatenated, separated by occurrences of the separator. If no separator is provided,
		// a single comma is used as the separator
		Array<Integer> x = Array(0, 1, 2, 3);
		assertToStringJoinEquals("0,1,2,3", x);
	}

	@Test
	public void testToString04() {
		Array<Integer> x = $array();
		x.$set(0, 0);
		x.$set(3, 3);
		assertToStringJoinEquals("0,,,3", x);
	}

	@Test
	public void testToString05() {
		Array<Integer> x = Array(null, 1, null, 3);
		assertToStringJoinEquals(",1,,3", x);
	}

	@Test
	public void testToString06() {
		Array<Integer> x = $array();
		x.$set(0, 0);
		assertToStringJoinEquals("0", x);
	}

	@Test
	public void testToString07() {
		// Operator use ToString from array arguments
		Array<String> s = Array("", "", "");
		assertToStringJoinEquals(",,", s);
	}

	@Test
	public void testToString08() {
		Array<String> s = Array("\\", "\\", "\\");
		assertToStringJoinEquals("\\,\\,\\", s);
	}

	@Test
	public void testToString09() {
		Array<String> s = Array("&", "&", "&");
		assertToStringJoinEquals("&,&,&", s);
	}

	@Test
	public void testToString10() {
		Array<Boolean> b = Array(true, true, true);
		assertToStringJoinEquals("true,true,true", b);
	}

	@Test
	public void testToString11() {
		Array<String> x = Array(null, null, null);
		assertToStringJoinEquals(",,", x);
	}

	@Test
	public void testToString12() {
		Array<Double> d = Array(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY);
		assertToStringJoinEquals("Infinity,Infinity,Infinity", d);
	}

	@Test
	public void testToString13() {
		Array<Double> d = Array(NaN, NaN, NaN);
		assertToStringJoinEquals("NaN,NaN,NaN", d); // BATMAAAN!
	}

	/**
	 * The test below is commented because it relies on the default implementation of ToString for Objects in
	 * JavaScript, which always returns "[object Object]". Since we don't have any way to properly emulate that behavior
	 * without causing inconsistencies, we just give up on supporting that.
	 */
	// @Test
	// public void testToString14() {
	// // If Type(value) is Object, evaluate ToPrimitive(value, String)
	// Object object = new Object() {
	// @SuppressWarnings("unused")
	// public String valueOf() {
	// return "+";
	// }
	// };
	// Array<Object> o = Array(object);
	// assertToStringJoinEquals("[object Object]", o);
	// }

	@Test
	public void testToString15() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public String valueOf() {
				return "+";
			}

			@Override
			public String toString() {
				return "*";
			}
		};
		Array<Object> o = Array(object);
		assertToStringJoinEquals("*", o);
	}

	@Test
	public void testToString16() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public String valueOf() {
				throw new RuntimeException("error");
			}

			@Override
			public String toString() {
				return "*";
			}
		};
		Array<Object> o = Array(object);
		assertToStringJoinEquals("*", o); // not supposed to throw an exception
	}

	@Test
	public void testToString17() {
		Object object = new Object() {
			@Override
			public String toString() {
				return "*";
			}
		};
		Array<Object> o = Array(object);
		assertToStringJoinEquals("*", o);
	}

	@Test
	public void testToString18() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public Object valueOf() {
				return new Object();
			}

			@Override
			public String toString() {
				return "*";
			}
		};
		Array<Object> o = Array(object);
		assertToStringJoinEquals("*", o);
	}

	@Test
	public void testToString19() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public String valueOf() {
				return "+";
			}

			@Override
			public String toString() {
				throw new RuntimeException("error");
			}
		};
		Array<Object> o = Array(object);
		try {
			o.toString();
			assertTrue(false);
		} catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}
	}

	private static void assertToStringJoinEquals(String expected, Array<?> array) {
		assertEquals(array.join(), array.toString());
		assertEquals(expected, array.toString());
	}
}
