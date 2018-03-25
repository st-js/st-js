package org.stjs.javascript;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.stjs.STJS;

/**
 * <p>JSGlobal class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@GlobalScope
public class JSGlobal {

	/** Constant <code>Infinity</code> */
	public static Number Infinity = Double.POSITIVE_INFINITY;
	/** Constant <code>NaN</code> */
	public static Number NaN = Double.NaN;
	/** Constant <code>undefined</code> */
	public static Object undefined;
	/** Constant <code>stjs</code> */
	public static STJS stjs;
	/** Constant <code>JSON</code> */
	public static JSON JSON;

	// The few methods below are actually part of the ECMA-262 standards, but there seems
	// to be no good way to use them using the Java syntax, so we have decided to them
	// If someone figures out a good, usable way to put them in, please send us a pull
	// request.

	// Also omitted: all the various Error constructor methods.

	// public static Object Object(){
	// return new Object();
	// }
	//
	// public static Object Object(Object value){
	// // TODO: code it
	// return null;
	// }
	//
	// public static Function Function(String... args){
	//
	// }

	/**
	 * Constructs a new empty <tt>Array</tt>.
	 *
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	public static <T> Array<T> Array() {
		return new Array<T>();
	}

	/**
	 * Constructs a new empty <tt>Array</tt> and sets it's <tt>length</tt> property to <tt>len</tt>.
	 *
	 * @param len
	 *            the length of this new array
	 * @param <T> a T object.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	public static <T> Array<T> Array(Number len) {
		return new Array<T>(len);
	}

	/**
	 * Constructs a new <tt>Array</tt> containing all the specified elements in the order in which they appear in the
	 * argument list.
	 *
	 * @param values
	 *            the values to add to this array, in the order in which they appear in the argument list
	 * @param <T> a T object.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	public static <T> Array<T> Array(T... values) {
		return new Array<T>(values);
	}

	/**
	 * <p>String.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String String() {
		return "";
	}

	/**
	 * <p>String.</p>
	 *
	 * @param arg a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String String(Object arg) {
		return JSAbstractOperations.ToString(arg);
	}

	/**
	 * <p>Boolean.</p>
	 *
	 * @param arg a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public static boolean Boolean(Object arg) {
		return JSAbstractOperations.ToBoolean(arg);
	}

	/**
	 * <p>Number.</p>
	 *
	 * @return a double.
	 */
	public static double Number() {
		return 0.0;
	}

	/**
	 * <p>Number.</p>
	 *
	 * @param arg a {@link java.lang.Object} object.
	 * @return a double.
	 */
	public static double Number(Object arg) {
		return JSAbstractOperations.ToNumber(arg);
	}

	/**
	 * <p>isNaN.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public static boolean isNaN(Object value) {
		if (value instanceof Number) {
			return Double.isNaN(((Number) value).doubleValue());
		}
		return false;
	}

	/**
	 * <p>Date.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date() {
		return new Date().toString();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year) {
		return Date();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @param month a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year, int month) {
		return Date();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param date a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year, int month, int date) {
		return Date();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param date a int.
	 * @param hours a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year, int month, int date, int hours) {
		return Date();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param date a int.
	 * @param hours a int.
	 * @param minutes a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year, int month, int date, int hours, int minutes) {
		return Date();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param date a int.
	 * @param hours a int.
	 * @param minutes a int.
	 * @param seconds a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year, int month, int date, int hours, int minutes, int seconds) {
		return Date();
	}

	/**
	 * <p>Date.</p>
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param date a int.
	 * @param hours a int.
	 * @param minutes a int.
	 * @param seconds a int.
	 * @param ms a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String Date(int year, int month, int date, int hours, int minutes, int seconds, int ms) {
		return Date();
	}

	/**
	 * <p>RegExp.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.RegExp} object.
	 */
	public static RegExp RegExp(String pattern) {
		return new RegExp(pattern);
	}

	/**
	 * <p>RegExp.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 * @param modifiers a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.RegExp} object.
	 */
	public static RegExp RegExp(String pattern, String modifiers) {
		return new RegExp(pattern, modifiers);
	}

	/**
	 * <p>eval.</p>
	 *
	 * @param expr a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public native static <T> T eval(String expr);

	/**
	 * <p>parseInt.</p>
	 *
	 * @param expr a {@link java.lang.Object} object.
	 * @return a int.
	 */
	public native static int parseInt(Object expr);

	/**
	 * <p>parseInt.</p>
	 *
	 * @param expr a {@link java.lang.Object} object.
	 * @param radix a int.
	 * @return a int.
	 */
	public native static int parseInt(Object expr, int radix);

	/**
	 * <p>parseFloat.</p>
	 *
	 * @param expr a {@link java.lang.Object} object.
	 * @return a double.
	 */
	public native static double parseFloat(Object expr);

	/**
	 * <p>isFinite.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public native static boolean isFinite(Object value);

	/**
	 * <p>decodeURI.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public native static String decodeURI(String uri);

	/**
	 * <p>decodeURIComponent.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public native static String decodeURIComponent(String uri);

	/**
	 * <p>encodeURI.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public native static String encodeURI(String uri);

	/**
	 * <p>encodeURIComponent.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public native static String encodeURIComponent(String uri);

	/**
	 * <p>typeof.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	@Template("typeOf")
	public native static String typeof(Object obj);

	/**
	 * defined in stjs.js
	 *
	 * @param exception a {@link java.lang.Object} object.
	 * @return a {@link java.lang.RuntimeException} object.
	 */
	public native static RuntimeException exception(Object exception);

	/**
	 * defined in stjs.js
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public native static boolean isEnum(Object obj);

	/**
	 * this is the equivalent of x || y || z in javascript
	 *
	 * @param value a T object.
	 * @param otherValues a T object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@Template("or")
	public native static <T> T $or(T value, T... otherValues);

}
