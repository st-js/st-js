package org.stjs.javascript;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Implements various abstract operations as defined in the ECMA-262 specification. The visibility of this class is
 * package private rather than public because the methods in this class are not intended to be called directly by user
 * code, but should be used for implementing a Java runtime equivalent of the core JavaScript APIs.
 * 
 * <p>
 * Note also that all the methods in this class are capitalized because the ECMA-262 specification defines them
 * capitalized (probably to set them appart from normal functions). We have decided to follow their convention to make
 * it clear when a real function is called and when an abstract operation is called.
 * 
 * @author lordofthepigs
 */
class JSAbstractOperations {

	private static final DecimalFormat PLAIN_INTEGER_FORMAT = new DecimalFormat("#0");
	private static final BigInteger UINT_MAX_VALUE = new BigInteger("4294967296"); // = 2^32
	private static final BigInteger SINT_MAX_VALUE = new BigInteger("2147483648"); // = 2^31
	private static final BigInteger USHORT_MAX_VALUE = new BigInteger("65536"); // = 2^16

	/**
	 * The [[DefaultValue]] internal method of Object, as close as possible to the definition in the ECMA-262
	 * specification section 8.12.8
	 * 
	 * <p>
	 * This implementation of <tt>[[DefaultValue]]</tt> differs slightly from the ECMA-262 specification When applied to
	 * Objects whose class does not override the <tt>toString()</tt> or <tt>valueOf()</tt> methods. In this case, this
	 * implementation returns a String which is equal to whatever is produced by <tt>java.lang.Object.toString()</tt>.
	 * Therefore, the returned string will not conform to the ECMA-262 specification which requires returning
	 * <tt>"[object Object]"</tt> in this case.
	 * 
	 * <p>
	 * This deviations is known and intentional. While it would have been possible to make the <tt>DefaultValue()</tt>
	 * implementation conform to ECMA-262, doing so would make it inconsistent with the result of the
	 * <tt>toString()</tt> method as defined by the java language. In order to minimize unexpected behavior,
	 * <tt>DefaultValue()</tt> behaves like <tt>toString()</tt> in those cases.
	 * 
	 * <p>
	 * Note that due to the fact that in Java toString() is guaranteed to return a String and is guaranteed to be
	 * callable, we cannot throw a TypeError from this method.
	 */
	static Object DefaultValue(Object arg) {
		return DefaultValue(arg, null);
	}

	/**
	 * The [[DefaultValue]] internal method of Object, as close as possible to the definition in the ECMA-262
	 * specification section 8.12.8
	 * 
	 * 
	 * <p>
	 * This implementation of <tt>[[DefaultValue]]</tt> differs slightly from the ECMA-262 specification When applied to
	 * Objects whose class does not override the <tt>toString()</tt> or <tt>valueOf()</tt> methods. In this case, this
	 * implementation returns a String which is equal to whatever is produced by <tt>java.lang.Object.toString()</tt>.
	 * Therefore, the returned string will not conform to the ECMA-262 specification which requires returning
	 * <tt>"[object Object]"</tt> in this case.
	 * 
	 * <p>
	 * This deviations is known and intentional. While it would have been possible to make the <tt>DefaultValue()</tt>
	 * implementation conform to ECMA-262, doing so would make it inconsistent with the result of the
	 * <tt>toString()</tt> method as defined by the java language. In order to minimize unexpected behavior,
	 * <tt>DefaultValue()</tt> behaves like <tt>toString()</tt> in those cases.
	 * 
	 * <p>
	 * Note that due to the fact that in Java toString() is guaranteed to return a String and is guaranteed to be
	 * callable, we cannot throw a TypeError from this method.
	 */
	static Object DefaultValue(Object arg, String hint) {
		if (hint == null) {
			if (arg instanceof Date) {
				return DefaultValue(arg, "String");
			} else {
				return DefaultValue(arg, "Number");
			}
		}

		if (hint.equals("String")) {
			try {
				// note: no need to check if the return value is a primitive type as the spec specifies,
				// because Java guarantees that we can only get a String, null or an exception
				return callToString(arg);
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException().getMessage(), e.getTargetException());
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}

			// toString is guaranteed to be callable and return a primitive value in Java, so we will
			// never need to call valueOf();

		} else if (hint.equals("Number")) {
			if (isValueOfCallable(arg)) {
				try {
					Object value = callValueOf(arg);
					if (isJsPrimitiveEquivalent(value)) {
						return value;
					}
				}
				catch (InvocationTargetException e) {
					throw new RuntimeException(e.getTargetException().getMessage(), e.getTargetException());
				}
				catch (Exception e) {
					// we can still get exception like SecurityException or IllegalAccessException here
					throw new RuntimeException(e);
				}
			}

			try {
				// note: no need to check if the return value is a primitive type as the spec specifies,
				// because Java guarantees that we can only get a String, null or an exception
				return callToString(arg);
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException().getMessage(), e.getTargetException());
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		throw new IllegalArgumentException("Unknown hint: " + hint);
	}

	private static String callToString(Object target) throws Exception {
		Method toString = target.getClass().getMethod("toString");
		if (toString.getDeclaringClass().equals(Object.class)) {
			// replace the of the toString method of Object as defined by java (which doesn't match JS
			// behavior) by the proper JS implementation
			// TODO: actually replace by the proper JS implementation. This might not even be a good idea.
			return (String) toString.invoke(target);

		}

		// some class in the hierarchy redefines toString(), so we can happily call it
		return (String) toString.invoke(target);
	}

	private static boolean isValueOfCallable(Object target) {
		try {
			return target.getClass().getMethod("valueOf") != null;
		}
		catch (Exception e) {
			return false;
		}
	}

	private static Object callValueOf(Object target) throws Exception {
		Method valueOf = target.getClass().getMethod("valueOf");
		return valueOf.invoke(target);
	}

	private static boolean isJsPrimitiveEquivalent(Object arg) {
		return arg == null || arg instanceof Boolean || arg instanceof String || arg instanceof Number;
	}

	/**
	 * The ToPrimitive() abstract operation, as close as possible to the definition in the ECMA-262 specification
	 * section 9.1.
	 * 
	 * <p>
	 * This implementation of <tt>ToPrimitive()</tt> differs slightly from the ECMA-262 specification When applied to
	 * Objects whose class does not override the <tt>toString()</tt> or <tt>valueOf()</tt> methods. In this case, this
	 * implementation returns a String which is equal to whatever is produced by <tt>java.lang.Object.toString()</tt>.
	 * Therefore, the returned string will not conform to the ECMA-262 specification which requires returning
	 * <tt>"[object Object]"</tt> in this case.
	 * 
	 * <p>
	 * This deviations is known and intentional. While it would have been possible to make the <tt>ToPrimitive()()</tt>
	 * implementation conform to ECMA-262, doing so would make it inconsistent with the result of the
	 * <tt>toString()</tt> method as defined by the java language. In order to minimize unexpected behavior,
	 * <tt>ToPrimitive()()</tt> behaves like <tt>toString()</tt> in those cases.
	 * 
	 * <p>
	 * This method returns Objects even though the Specification says that the values should be JS primitives, but when
	 * actually used JS primitives really behave the same a their wrapper object (similar to auto-boxing in Java), so we
	 * can get away with returning the corresponding Object.
	 */
	static Object ToPrimitive(Object arg) {
		return ToPrimitive(arg, "Number");
	}

	/**
	 * The ToPrimitive() abstract operation, as close as possible to the definition in the ECMA-262 specification
	 * section 9.1.
	 * 
	 * <p>
	 * This implementation of <tt>ToPrimitive()</tt> differs slightly from the ECMA-262 specification When applied to
	 * Objects whose class does not override the <tt>toString()</tt> or <tt>valueOf()</tt> methods. In this case, this
	 * implementation returns a String which is equal to whatever is produced by <tt>java.lang.Object.toString()</tt>.
	 * Therefore, the returned string will not conform to the ECMA-262 specification which requires returning
	 * <tt>"[object Object]"</tt> in this case.
	 * 
	 * <p>
	 * This deviations is known and intentional. While it would have been possible to make the <tt>ToPrimitive()()</tt>
	 * implementation conform to ECMA-262, doing so would make it inconsistent with the result of the
	 * <tt>toString()</tt> method as defined by the java language. In order to minimize unexpected behavior,
	 * <tt>ToPrimitive()()</tt> behaves like <tt>toString()</tt> in those cases.
	 * 
	 * <p>
	 * This method returns Objects even though the Specification says that the values should be JS primitives, but when
	 * actually used JS primitives really behave the same a their wrapper object (similar to auto-boxing in Java), so we
	 * can get away with returning the corresponding Object.
	 */
	static Object ToPrimitive(Object arg, String hint) {
		if (arg == null || arg instanceof Boolean || arg instanceof Number || arg instanceof String) {
			return arg;
		}

		return DefaultValue(arg, hint);
	}

	/**
	 * The ToBoolean() abstract operation, as defined in the ECMA-262 specification section 9.2.
	 */
	static Boolean ToBoolean(Object arg) {
		if (arg == null) {
			return Boolean.FALSE;

		} else if (arg instanceof Boolean) {
			return (Boolean) arg;

		} else if (arg instanceof Number) {
			double value = ((Number) arg).doubleValue();
			return !(Double.isNaN(value) || value == 0.0);

		} else if (arg instanceof String) {
			return !"".equals(arg);
		}
		return true;
	}

	/**
	 * The ToNumber() abstract operation, as defined in the ECMA-262 specification section 9.3.
	 */
	static Double ToNumber(Object arg) {
		if (arg == null) {
			return 0.0d;

		} else if (Boolean.TRUE.equals(arg)) {
			return 1.0d;

		} else if (Boolean.FALSE.equals(arg)) {
			return 0.0d;

		} else if (arg instanceof String) {
			return ToNumber((String) arg);

		} else if (arg instanceof Number) {
			return ((Number) arg).doubleValue();
		}

		Object primValue = ToPrimitive(arg, "Number");
		return ToNumber(primValue).doubleValue();
	}

	/**
	 * The ToNumber() abstract operation applied to String, as defined in the ECMA-262 specification section 9.3.1
	 */
	static Double ToNumber(String arg) {
		if (arg == null) {
			return 0.0d;
		}
		String trimmed = ((String) arg).trim();
		if (trimmed.isEmpty()) {
			return 0.0d;

		} else if ("Infinity".equals(arg)) {
			return Double.POSITIVE_INFINITY;
		}

		if (trimmed.startsWith("0x") || trimmed.startsWith("0X")) {
			// parse as a hexadecimal digit
			try {
				return Long.decode(trimmed).doubleValue();
			}
			catch (NumberFormatException e) {
				return Double.NaN;
			}
		}

		// parse as a decimal number
		try {
			return Double.parseDouble(trimmed);
		}
		catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	/**
	 * The ToInteger() abstract operation, as defined in the ECMA-262 specification section 9.4. Even though this
	 * operation is called ToInteger(), it still returns a Double, because it needs to be able to represent values such
	 * as NaN, +Infinity and -Infinity.
	 */
	static Double ToInteger(Object arg) {
		Double number = ToNumber(arg);
		if (number.isNaN()) {
			return 0.0;
		}
		if (number.isInfinite()) {
			return number;
		}

		return java.lang.Math.signum(number) * java.lang.Math.floor(java.lang.Math.abs(number));
	}

	/**
	 * The ToInt32() abstract operation, as defined in the ECMA-262 specification section 9.5
	 */
	static Double ToInt32(Object arg) {
		Double number = ToNumber(arg);
		if (number.isNaN() || number.isInfinite() || number == 0.0) {
			return 0.0;
		}

		Double posInt = ToInteger(number);

		// here we have to use BigInteger, because posInt could easily be waaay out of range for int
		// and the spec specifies how to handle that case
		BigInteger posIntBig = new BigInteger(PLAIN_INTEGER_FORMAT.format(posInt));
		BigInteger int32bit = posIntBig.mod(UINT_MAX_VALUE);

		if (int32bit.compareTo(SINT_MAX_VALUE) >= 0) {
			return int32bit.subtract(UINT_MAX_VALUE).doubleValue();
		}
		return int32bit.doubleValue();
	}

	/**
	 * The ToUInt32() abstract operation, as defined in the ECMA-262 specification section 9.6
	 */
	static Double ToUInt32(Object arg) {
		Double number = ToNumber(arg);
		if (number.isNaN() || number.isInfinite() || number == 0.0) {
			return 0.0;
		}

		Double posInt = ToInteger(number);

		// here we have to use BigInteger, because posInt could easily be waaay out of range for int
		// and the spec specifies how to handle that case
		BigInteger posIntBig = new BigInteger(PLAIN_INTEGER_FORMAT.format(posInt));
		BigInteger int32bit = posIntBig.mod(UINT_MAX_VALUE);
		return int32bit.doubleValue();
	}

	/**
	 * The ToUInt16() abstract operation, as defined in the ECMA-262 specification section 9.7
	 */
	static Double ToUInt16(Object arg) {
		Double number = ToNumber(arg);
		if (number.isNaN() || number.isInfinite() || number == 0.0) {
			return 0.0;
		}

		Double posInt = ToInteger(number);

		// here we have to use BigInteger, because posInt could easily be waaay out of range for int
		// and the spec specifies how to handle that case
		BigInteger posIntBig = new BigInteger(PLAIN_INTEGER_FORMAT.format(posInt));
		BigInteger int16bit = posIntBig.mod(USHORT_MAX_VALUE);
		return int16bit.doubleValue();
	}

	/**
	 * The <tt>ToString()</tt> abstract operation, as close as possible to the definition in the ECMA-262 specification
	 * section 9.8.
	 * 
	 * <p>
	 * This implementation of <tt>ToString()</tt> differs slightly from the ECMA-262 specification in the following
	 * manner:
	 * <ul>
	 * <li>When <tt>ToString()</tt> is applied to Objects whose class does not override the <tt>toString()</tt> method,
	 * then the returned string is equal to whatever is produced by <tt>java.lang.Object.toString()</tt>. Therefore, the
	 * returned string will not conform to the ECMA-262 specification which requires returning
	 * <tt>"[object Object]"</tt> in this case.
	 * <li><tt>ToString(0)</tt> returns <tt>"0"</tt> and to <tt>ToString(0.0)</tt> return <tt>"0.0"</tt>, which are not
	 * equal. The ECMA-262 specification mentions a very specific algorithm for converting numbers to string, which due
	 * to the difference between the various subclasses of <tt>java.lang.Number</tt> is not fully respected by this
	 * implementation
	 * </ul>
	 * <p>
	 * Both of these deviations are known and intentional. While it would have been possible to make the
	 * <tt>ToString()</tt> operation conform to ECMA-262, doing so would make it inconsistent with the result of the
	 * <tt>toString()</tt> method as defined by the java language. In order to minimize unexpected behavior,
	 * <tt>ToString()</tt> behaves like <tt>toString()</tt> in those cases.
	 */
	static String ToString(Object arg) {
		if (arg == null) {
			return "null";

		} else if (Boolean.TRUE.equals(arg)) {
			return "true";

		} else if (Boolean.FALSE.equals(arg)) {
			return "false";

		} else if (arg instanceof String) {
			return (String) arg;

		} else if (arg instanceof Number) {
			return ToString((Number) arg);

		}

		Object primValue = ToPrimitive(arg, "String");
		return ToString(primValue);
	}

	/**
	 * The ToString() abstract operation for Numbers, as close as possible to the definition in the ECMA-262
	 * specification section 9.8.1.
	 * 
	 * <p>
	 * This implementation of <tt>ToString()</tt> differs slightly from the ECMA-262 specification. <tt>ToString(0)</tt>
	 * returns <tt>"0"</tt> and to <tt>ToString(0.0)</tt> return <tt>"0.0"</tt>, which are not equal. The ECMA-262
	 * specification mentions a very specific algorithm for converting numbers to string, which due to the difference
	 * between the various subclasses of <tt>java.lang.Number</tt> is not fully respected by this implementation
	 * <p>
	 * This deviation is known and intentional. While it would have been possible to make the <tt>ToString()</tt>
	 * operation conform to ECMA-262, doing so would make it inconsistent with the result of the <tt>toString()</tt>
	 * method as defined by the java language. In order to minimize unexpected behavior, <tt>ToString()</tt> behaves
	 * like <tt>toString()</tt> in those cases.
	 */
	static String ToString(Number arg) {
		if (arg == null) {
			return "null";
		}

		return arg.toString();
	}

	/**
	 * The ToObject() operation, as defined in the ECMA-262 specification section 9.9
	 */
	static Object ToObject(Object arg) {
		if (arg == null) {
			throw new RuntimeException("TypeError");
		}
		return arg;
	}

	/**
	 * The CheckObjectCoercible operation, as defined in the ECMA-262 specification section 9.10
	 */
	static boolean CheckObjectCoercible(Object arg) {
		return arg != null;
	}
}
