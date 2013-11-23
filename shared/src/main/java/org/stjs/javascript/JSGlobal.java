package org.stjs.javascript;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.stjs.STJS;

@GlobalScope
public class JSGlobal {

	public static Number Infinity = Double.POSITIVE_INFINITY;
	public static Number NaN = Double.NaN;
	public static Object undefined;
	public static STJS stjs;
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

	public static <T> Array<T> Array() {
		@SuppressWarnings("unchecked")
		Array<T> array = JSCollections.$array();
		return array;
	}

	public static <T> Array<T> Array(int size) {
		@SuppressWarnings("unchecked")
		Array<T> array = JSCollections.$array();
		array.$length(size);
		return array;
	}

	public static <T> Array<T> Array(T first, T second, T... others) {
		Object[] params = new Object[others.length + 2];
		params[0] = first;
		params[1] = second;
		System.arraycopy(others, 0, params, 2, others.length);

		@SuppressWarnings("unchecked")
		Array<T> array = JSCollections.$array((T[]) params);
		return array;
	}

	public static String String() {
		return "";
	}

	public static String String(Object arg) {
		return JSAbstractOperations.ToString(arg);
	}

	public static boolean Boolean(Object arg) {
		return JSAbstractOperations.ToBoolean(arg);
	}

	public static double Number() {
		return 0.0;
	}

	public static double Number(Object arg) {
		return JSAbstractOperations.ToNumber(arg);
	}

	public static boolean isNaN(Object value) {
		if (value instanceof Number) {
			return Double.isNaN(((Number) value).doubleValue());
		}
		return false;
	}

	public static String Date() {
		return new Date().toString();
	}

	public static String Date(int year) {
		return Date();
	}

	public static String Date(int year, int month) {
		return Date();
	}

	public static String Date(int year, int month, int date) {
		return Date();
	}

	public static String Date(int year, int month, int date, int hours) {
		return Date();
	}

	public static String Date(int year, int month, int date, int hours, int minutes) {
		return Date();
	}

	public static String Date(int year, int month, int date, int hours, int minutes, int seconds) {
		return Date();
	}

	public static String Date(int year, int month, int date, int hours, int minutes, int seconds, int ms) {
		return Date();
	}

	public static RegExp RegExp(String pattern) {
		return new RegExp(pattern);
	}

	public static RegExp RegExp(String pattern, String modifiers) {
		return new RegExp(pattern, modifiers);
	}

	public native static <T> T eval(String expr);

	public native static int parseInt(Object expr);

	public native static int parseInt(Object expr, int radix);

	public native static double parseFloat(Object expr);

	public native static boolean isFinite(Object value);

	public native static String decodeURI(String uri);

	public native static String decodeURIComponent(String uri);

	public native static String encodeURI(String uri);

	public native static String encodeURIComponent(String uri);

	@Template("typeOf")
	public native static String typeof(Object obj);

	/**
	 * defined in stjs.js
	 * 
	 * @param exception
	 * @return
	 */
	public native static RuntimeException exception(Object exception);

	/**
	 * defined in stjs.js
	 * 
	 * @param obj
	 * @return
	 */
	public native static boolean isEnum(Object obj);

	/**
	 * this is the equivalent of x || y || z in javascript
	 * 
	 * @return
	 */
	@Template("or")
	public native static <T> T $or(T value, T... otherValues);

}
