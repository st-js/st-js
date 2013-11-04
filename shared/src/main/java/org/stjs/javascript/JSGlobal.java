package org.stjs.javascript;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.stjs.STJS;

@GlobalScope
public class JSGlobal {

	public static Number Infinity;
	public static Number NaN;
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
		// TODO: code it
		return null;
	}

	public static <T> Array<T> Array(int size) {
		// TODO: code it
		return null;
	}

	public static <T> Array<T> Array(T first, T second, T... others) {
		// TODO: code it
		return null;
	}

	public static String String() {
		return "";
	}

	public static String String(Object arg) {
		// TODO: code it
		return null;
	}

	public static boolean Boolean(Object arg) {
		// TODO: code it
		return false;
	}

	public static double Number() {
		return 0.0;
	}

	public static double Number(Object arg) {
		// TODO: code it
		return 0.0;
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
}
