package org.stjs.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.stjs.javascript.JSAbstractOperations.DefaultValue;
import static org.stjs.javascript.JSAbstractOperations.ToBoolean;
import static org.stjs.javascript.JSAbstractOperations.ToInt32;
import static org.stjs.javascript.JSAbstractOperations.ToInteger;
import static org.stjs.javascript.JSAbstractOperations.ToNumber;
import static org.stjs.javascript.JSAbstractOperations.ToObject;
import static org.stjs.javascript.JSAbstractOperations.ToPrimitive;
import static org.stjs.javascript.JSAbstractOperations.ToString;
import static org.stjs.javascript.JSAbstractOperations.ToUInt16;
import static org.stjs.javascript.JSAbstractOperations.ToUInt32;
import static org.stjs.javascript.JSGlobal.Array;
import static org.stjs.javascript.JSGlobal.NaN;
import static org.stjs.javascript.JSGlobal.Number;
import static org.stjs.javascript.JSGlobal.isNaN;

import org.junit.Test;

public class JSAbstractOperationsTest {

	@Test
	public void testDefaultValue01() {
		Object o = new Object() {
			public String toString() {
				return "1";
			}

			@SuppressWarnings("unused")
			public Object valueOf() {
				return new Object();
			}
		};
		assertEquals("1", DefaultValue(o));
	}

	@Test
	public void testToPrimitive01() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public String valueOf() {
				return "1";
			}

			public String toString() {
				return "0";
			}
		};

		assertEquals("1", ToPrimitive(object));
	}

	@Test
	public void testToPrimitive02() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public Object valueOf() {
				return new Object();
			}

			public String toString() {
				return "0";
			}
		};

		assertEquals("0", ToPrimitive(object));
	}

	@Test
	public void testToPrimitive03() {
		Object object = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 0;
			}

			public String toString() {
				return "1";
			}
		};

		assertEquals("1", ToPrimitive(object, "String"));
	}

	@Test
	public void testToBoolean01() {
		assertEquals(false, ToBoolean(null));
	}

	@Test
	public void testToBoolean02() {
		assertEquals(true, ToBoolean(true));
		assertEquals(false, ToBoolean(false));
	}

	@Test
	public void testToBoolean03() {
		assertEquals(false, ToBoolean(+0));
		assertEquals(false, ToBoolean(-0));
		assertEquals(false, ToBoolean(NaN));
	}

	@Test
	public void testToBoolean04() {
		assertEquals(true, ToBoolean(Double.POSITIVE_INFINITY));
		assertEquals(true, ToBoolean(Double.NEGATIVE_INFINITY));
		assertEquals(true, ToBoolean(Double.MAX_VALUE));
		assertEquals(true, ToBoolean(Double.MIN_VALUE));
		assertEquals(true, ToBoolean(13));
		assertEquals(true, ToBoolean(-13));
		assertEquals(true, ToBoolean(1.3));
		assertEquals(true, ToBoolean(-1.3));
	}

	@Test
	public void testToBoolean05() {
		assertEquals(false, ToBoolean(""));
	}

	@Test
	public void testToBoolean06() {
		assertEquals(true, ToBoolean(" "));
		assertEquals(true, ToBoolean("Nonempty String"));
	}

	@Test
	public void testToNumber01() {
		assertEquals(0.0, ToNumber(null), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToNumber(null), 0.0);
	}

	@Test
	public void testToNumber02() {
		assertEquals(0.0, ToNumber(false), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToNumber(false), 0.0);
		assertEquals(1.0, ToNumber(true), 1.0);
	}

	@Test
	public void testToNumber03() {
		assertEquals(13.0, ToNumber(13), 0.0);
		assertEquals(-13.0, ToNumber(-13), 0.0);
		assertEquals(1.3, ToNumber(1.3), 0.0);
		assertEquals(-1.3, ToNumber(-1.3), 0.0);
		assertEquals(1.7976931348623157e308, ToNumber(Double.MAX_VALUE), 0.0);
		assertEquals(5e-324, ToNumber(Double.MIN_VALUE), 0.0);
		assertTrue(isNaN(ToNumber(NaN)));
		assertEquals(0.0, ToNumber(+0.0), 0.0);
		assertEquals(-0.0, ToNumber(-0.0), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, ToNumber(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(Double.NEGATIVE_INFINITY, ToNumber(Double.NEGATIVE_INFINITY), 0.0);
	}

	@Test
	public void testToNumber04() {
		assertEquals(1, ToNumber(true), 0.0);
		assertEquals(+0, ToNumber(false), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / Number(false), 0.0);

		Object o = new Object() {
			@SuppressWarnings("unused")
			public double ToNumber() {
				return 123456;
			}

			public String toString() {
				return "67890";
			}

			@SuppressWarnings("unused")
			public String valueOf() {
				return "[object MyObj]";
			}
		};
		assertTrue(isNaN(ToNumber(o)));

		o = new Object() {
			@SuppressWarnings("unused")
			public double ToNumber() {
				return 123456;
			}

			public String toString() {
				return "67890";
			}

			@SuppressWarnings("unused")
			public String valueOf() {
				return "9876543210";
			}
		};
		assertEquals(9876543210d, ToNumber(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public double ToNumber() {
				return 123456;
			}

			public String toString() {
				return "[object MyObj]";
			}
		};
		assertTrue(isNaN(ToNumber(o)));

		o = new Object() {
			@SuppressWarnings("unused")
			public double ToNumber() {
				return 123456;
			}

			public String toString() {
				return "67890";
			}
		};
		assertEquals(67890d, ToNumber(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public double ToNumber() {
				return 123456;
			}
		};
		assertTrue(isNaN(ToNumber(o)));
	}

	@Test
	public void testToInteger01() {
		assertEquals(0.0, ToInteger(NaN), 0.0);
		assertEquals(0.0, ToInteger("x"), 0.0);
		assertEquals(0.0, ToInteger(+0), 0.0);
		assertEquals(0.0, ToInteger(-0), 0.0);
	}

	@Test
	public void testToInteger02() {
		assertEquals(6, ToInteger(6.54321), 0.0);
		assertEquals(-6, ToInteger(-6.54321), 0.0);
		assertEquals(654, ToInteger(6.54321e2), 0.0);
		assertEquals(-654, ToInteger(-6.54321e2), 0.0);
		assertEquals(6, ToInteger(0.654321e1), 0.0);
		assertEquals(-6, ToInteger(-0.654321e1), 0.0);
		assertEquals(1, ToInteger(true), 0.0);
		assertEquals(0, ToInteger(false), 0.0);
		assertEquals(1.23e15, ToInteger(1.23e15), 0.0);
		assertEquals(-1.23e15, ToInteger(-1.23e15), 0.0);
		assertEquals(0, ToInteger(1.23e-15), 0.0);
		assertEquals(-0, ToInteger(-1.23e-15), 0.0);
	}

	@Test
	public void testToInteger03() {
		assertEquals(Double.POSITIVE_INFINITY, ToInteger(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(Double.NEGATIVE_INFINITY, ToInteger(Double.NEGATIVE_INFINITY), 0.0);
		assertEquals(0.0, ToInteger(0.0), 0.0);
		assertEquals(0.0, ToInteger(0.0), 0.0);
		assertEquals(-0.0, ToInteger(-0.0), 0.0);
	}

	@Test
	public void testToInt3201() {
		assertEquals(0.0, ToInt32(NaN), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToInt32(NaN), 0.0);
		assertEquals(0.0, ToInt32(0), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToInt32(0), 0.0);
		assertEquals(0.0, ToInt32(-0), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToInt32(-0), 0.0);
		assertEquals(0.0, ToInt32(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToInt32(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(0.0, ToInt32(Double.NEGATIVE_INFINITY), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, 1 / ToInt32(Double.NEGATIVE_INFINITY), 0.0);
	}

	@Test
	public void testToInt3202() {
		assertEquals(-2147483647d, ToInt32(-2147483647d), 0.0);
		assertEquals(-2147483648d, ToInt32(-2147483648d), 0.0);
		assertEquals(2147483647d, ToInt32(-2147483649d), 0.0);
		assertEquals(0d, ToInt32(-4294967296d), 0.0);
		assertEquals(1d, ToInt32(-4294967295d), 0.0);
		assertEquals(-1d, ToInt32(-4294967297d), 0.0);
		assertEquals(2147483646d, ToInt32(2147483646d), 0.0);
		assertEquals(2147483647d, ToInt32(2147483647d), 0.0);
		assertEquals(-2147483648d, ToInt32(2147483648d), 0.0);
		assertEquals(0d, ToInt32(4294967296d), 0.0);
		assertEquals(-1d, ToInt32(4294967295d), 0.0);
		assertEquals(1d, ToInt32(4294967297d), 0.0);
		assertEquals(-1d, ToInt32(8589934591d), 0.0);
		assertEquals(0d, ToInt32(8589934592d), 0.0);
		assertEquals(1d, ToInt32(8589934593d), 0.0);
	}

	@Test
	public void testToInt3203() {
		assertEquals(1, ToInt32(true), 0.0);
		assertEquals(0, ToInt32(false), 0.0);
		assertEquals(-1, ToInt32(-1.234), 0.0);
		assertEquals(1, ToInt32(1.234), 0.0);
		assertEquals(-1, ToInt32("-1.234"), 0.0);
	}

	@Test
	public void testToInt3204() {
		Object o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}
		};
		assertEquals(1.0, ToInt32(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			};

			public String toString() {
				return "0";
			}
		};
		assertEquals(1.0, ToInt32(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			};

			public String toString() {
				throw new RuntimeException("Exception that should not be thrown");
			}
		};
		assertEquals(1.0, ToInt32(o), 0.0);

		o = new Object() {
			public String toString() {
				return "1";
			}
		};
		assertEquals(1.0, ToInt32(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public Object valueOf() {
				return new Object();
			}

			public String toString() {
				return "1";
			}
		};
		assertEquals(1.0, ToInt32(o), 0.0);

		try {
			o = new Object() {
				@SuppressWarnings("unused")
				public Object valueOf() {
					throw new RuntimeException("error");
				}

				public String toString() {
					return "1";
				}
			};
			ToInt32(o);
			throw new RuntimeException("Expected an exception, but got nothing");
		}
		catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}

		try {
			o = new Object() {
				public String toString() {
					throw new RuntimeException("error");
				}
			};
			ToInt32(o);
			throw new RuntimeException("Expected an exception, but got nothing");
		}
		catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}
	}

	@Test
	public void testToUInt3201() {
		assertEquals(0.0, ToUInt32(NaN), 0.0);
		assertEquals(0.0, ToUInt32("abc"), 0.0);
		assertEquals(+0.0, ToUInt32(+0.0), 0.0);
		assertEquals(+0.0, ToUInt32(-0.0), 0.0);
		assertEquals(0.0, ToUInt32(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(0.0, ToUInt32(Double.NEGATIVE_INFINITY), 0.0);
	}

	@Test
	public void testToUInt3202() {
		assertEquals(1.0, ToUInt32(1.2345), 0.0);
		assertEquals(4294967291.0, ToUInt32(-5.4321), 0.0);

		assertEquals(0.0, ToUInt32(0.0), 0.0);
		assertEquals(1.0, ToUInt32(1), 0.0);
		assertEquals(4294967295.0, ToUInt32(-1), 0.0);
		assertEquals(4294967295.0, ToUInt32(4294967295.0), 0.0);
		assertEquals(4294967294.0, ToUInt32(4294967294.0), 0.0);
		assertEquals(0.0, ToUInt32(4294967296.0), 0.0);

		assertEquals(2147483649.0, ToUInt32(-2147483647.0), 0.0);
		assertEquals(2147483648.0, ToUInt32(-2147483648.0), 0.0);
		assertEquals(2147483647.0, ToUInt32(-2147483649.0), 0.0);
		assertEquals(1.0, ToUInt32(-4294967295.0), 0.0);
		assertEquals(0.0, ToUInt32(-4294967296.0), 0.0);
		assertEquals(4294967295.0, ToUInt32(-4294967297.0), 0.0);
		assertEquals(4294967295.0, ToUInt32(4294967295.0), 0.0);
		assertEquals(0.0, ToUInt32(4294967296.0), 0.0);
		assertEquals(1.0, ToUInt32(4294967297.0), 0.0);
		assertEquals(4294967295.0, ToUInt32(8589934591.0), 0.0);
		assertEquals(0.0, ToUInt32(8589934592.0), 0.0);
		assertEquals(1.0, ToUInt32(8589934593.0), 0.0);
	}

	@Test
	public void testToUInt3203() {
		assertEquals(1.0, ToUInt32(true), 0.0);
		assertEquals(0.0, ToUInt32(false), 0.0);
		assertEquals(1.0, ToUInt32("1"), 0.0);
		assertEquals(4294967295.0, ToUInt32("-1.234"), 0.0);
	}

	@Test
	public void testUInt3204() {
		Object o;

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}
		};
		assertEquals(1.0, ToUInt32(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}

			public String toString() {
				return "0";
			}
		};
		assertEquals(1.0, ToUInt32(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}

			public String toString() {
				throw new RuntimeException("error");
			}
		};
		assertEquals(1.0, ToUInt32(o), 0.0);

		o = new Object() {
			public String toString() {
				return "1";
			}
		};
		assertEquals(1.0, ToUInt32(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public Object valueOf() {
				return new Object();
			}

			public String toString() {
				return "1";
			}
		};
		assertEquals(1.0, ToUInt32(o), 0.0);

		try {
			o = new Object() {
				@SuppressWarnings("unused")
				public Object valueOf() {
					throw new RuntimeException("error");
				}

				public String toString() {
					return "1";
				}
			};
			ToUInt32(o);
			throw new RuntimeException("Expected an exception, but got nothing");
		}
		catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}

		try {
			o = new Object() {
				public String toString() {
					throw new RuntimeException("error");
				}
			};
			ToUInt32(o);
			throw new RuntimeException("Expected an exception, but got nothing");
		}
		catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}
	}

	@Test
	public void testToUInt1601() {
		assertEquals(0.0, ToUInt16(NaN), 0.0);
		assertEquals(0.0, ToUInt16(0.0), 0.0);
		assertEquals(0.0, ToUInt16(-0.0), 0.0);
		assertEquals(0.0, ToUInt16(Double.POSITIVE_INFINITY), 0.0);
		assertEquals(0.0, ToUInt16(Double.NEGATIVE_INFINITY), 0.0);
	}

	@Test
	public void testToUInt1602() {
		assertEquals(0.0, ToUInt16(0.0), 0.0);
		assertEquals(1.0, ToUInt16(1.0), 0.0);
		assertEquals(65535.0, ToUInt16(-1.0), 0.0);
		assertEquals(65534.0, ToUInt16(65534.0), 0.0);
		assertEquals(65535.0, ToUInt16(65535.0), 0.0);
		assertEquals(0.0, ToUInt16(65536.0), 0.0);
		assertEquals(65535.0, ToUInt16(4294967295.0), 0.0);
		assertEquals(65534.0, ToUInt16(4294967294.0), 0.0);
		assertEquals(0.0, ToUInt16(4294967296.0), 0.0);

		assertEquals(32769.0, ToUInt16(-32767.0), 0.0);
		assertEquals(32768.0, ToUInt16(-32768.0), 0.0);
		assertEquals(32767.0, ToUInt16(-32769.0), 0.0);
		assertEquals(1.0, ToUInt16(-65535.0), 0.0);
		assertEquals(0.0, ToUInt16(-65536.0), 0.0);
		assertEquals(65535.0, ToUInt16(-65537.0), 0.0);
		assertEquals(65535.0, ToUInt16(65535.0), 0.0);
		assertEquals(0.0, ToUInt16(65536.0), 0.0);
		assertEquals(1.0, ToUInt16(65537.0), 0.0);
		assertEquals(65535.0, ToUInt16(131071.0), 0.0);
		assertEquals(0.0, ToUInt16(131072.0), 0.0);
		assertEquals(1.0, ToUInt16(131073.0), 0.0);
	}

	@Test
	public void testToUInt1603() {
		assertEquals(1.0, ToUInt16(true), 0.0);
		assertEquals(0.0, ToUInt16(false), 0.0);
		assertEquals(65535.0, ToUInt16(-1.234), 0.0);
		assertEquals(1.0, ToUInt16(1.234), 0.0);
		assertEquals(65531.0, ToUInt16(-5.4321), 0.0);
		assertEquals(1.0, ToUInt16("1"), 0.0);
		assertEquals(65535.0, ToUInt16("-1.234"), 0.0);
	}

	@Test
	public void testUInt1604() {
		Object o;

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}
		};
		assertEquals(1.0, ToUInt16(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}

			public String toString() {
				return "0";
			}
		};
		assertEquals(1.0, ToUInt16(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public int valueOf() {
				return 1;
			}

			public String toString() {
				throw new RuntimeException("error");
			}
		};
		assertEquals(1.0, ToUInt16(o), 0.0);

		o = new Object() {
			public String toString() {
				return "1";
			}
		};
		assertEquals(1.0, ToUInt16(o), 0.0);

		o = new Object() {
			@SuppressWarnings("unused")
			public Object valueOf() {
				return new Object();
			}

			public String toString() {
				return "1";
			}
		};
		assertEquals(1.0, ToUInt16(o), 0.0);

		try {
			o = new Object() {
				@SuppressWarnings("unused")
				public Object valueOf() {
					throw new RuntimeException("error");
				}

				public String toString() {
					return "1";
				}
			};
			ToUInt16(o);
			throw new RuntimeException("Expected an exception, but got nothing");
		}
		catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}

		try {
			o = new Object() {
				public String toString() {
					throw new RuntimeException("error");
				}
			};
			ToUInt16(o);
			throw new RuntimeException("Expected an exception, but got nothing");
		}
		catch (RuntimeException e) {
			assertEquals("error", e.getMessage());
		}
	}

	@Test
	public void testToString01() {
		assertEquals("null", ToString(null));
		assertEquals("false", ToString(false));
		assertEquals("true", ToString(true));
		assertEquals("abc", ToString("abc"));
		assertEquals("0", ToString(0));
		assertEquals("NaN", ToString(NaN));
	}

	@Test
	public void testToString02() {
		assertEquals("2,4,8,16,32", ToString(Array(2, 4, 8, 16, 32)));

		// CHECK#11
		Object myobj1 = new Object() {
			@SuppressWarnings("unused")
			public double toNumber() {
				return 12345;
			}

			public String toString() {
				return "67890";
			}

			@SuppressWarnings("unused")
			public String valueOf() {
				return "[object MyObj]";
			}
		};
		assertEquals("67890", ToString(myobj1));
	}

	@Test
	public void testToString03() {
		assertEquals("0", ToString(+0));
		assertEquals("0", ToString(-0));
		assertEquals("-1234567890", ToString(-1234567890));
		assertEquals("Infinity", ToString(Double.POSITIVE_INFINITY));
		assertEquals("-Infinity", ToString(Double.NEGATIVE_INFINITY));

		assertEquals("1", ToString(1));
		assertEquals("10", ToString(10));
		assertEquals("100", ToString(100));
		assertEquals("12345", ToString(12345));
		assertEquals("12345000", ToString(12345000));
		assertEquals("-1", ToString(-1));
		assertEquals("-10", ToString(-10));
		assertEquals("-100", ToString(-100));
		assertEquals("-12345", ToString(-12345));
		assertEquals("-12345000", ToString(-12345000));

		assertEquals("1.0000001", ToString(1.0000001));
		assertEquals("-1.0000001", ToString(-1.0000001));

		assertEquals("0.1", ToString(0.1));
		assertEquals("-0.1", ToString(-0.1));

		// Checks below do not apply due to incompatibilities between Java and ECMA-262. See method documentations
		// assertEquals("100000000000000000000", ToString(1e20)); => actually returns "1.0e20"
		// assertEquals("1e+21", ToString(1e21)); => actually returns "1.0e+21"
		// assertEquals("0.000001", ToString(0.000001)); => actually returns "1.0e-6"
	}

	@Test(
			expected = RuntimeException.class)
	public void testToObject01() {
		ToObject(null);
	}

	@Test
	public void testToObject02() {
		assertSameAfterToObject(new Object());
		assertSameAfterToObject(1);
		assertSameAfterToObject(1L);
		assertSameAfterToObject(1F);
		assertSameAfterToObject(1D);
		assertSameAfterToObject(true);
		assertSameAfterToObject("abc");
	}

	private void assertSameAfterToObject(Object o) {
		assertSame(o, ToObject(o));
	}
}
