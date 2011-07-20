package org.stjs.javascript;

import org.stjs.javascript.jquery.GlobalJQuery;
import org.stjs.javascript.jquery.JQueryAndPlugins;

public class Global {
	public static GlobalJQuery $;

	public static Number Infinity;
	public static Number NaN;

	public static Object undefined;

	public static Window window;

	// do not add this one too
	// public static Window self;

	/**
	 * jquery constructors
	 */
	public static JQueryAndPlugins $(String path) {
		// return new JQuery(path);
		return null;
	}

	public static JQueryAndPlugins $(Object path) {
		// return new JQuery(path);
		return null;
	}

	public static <T> T eval(String expr) {
		return null;
	}

	public static void alert(Object expr) {
	}

	public static boolean confirm(String expr) {
		return true;
	}

	public static String prompt(String expr) {
		return null;
	}

	public static String prompt(String expr, String defaultText) {
		return null;
	}

	public static double parseFloat(Object expr) {
		return 0;
	}

	public static int parseInt(Object expr) {
		return 0;
	}

	public static <T> T nvl(T value, T valueIfNull) {
		return value;
	}

	public static TimeoutHandler setTimeout(String expr, int timeoutMillis) {
		return null;
	}

	public static TimeoutHandler setTimeout(Runnable expr, int timeoutMillis) {
		return null;
	}

	public static TimeoutHandler setInterval(String expr, int timeoutMillis) {
		return null;
	}

	public static TimeoutHandler setInterval(Runnable expr, int timeoutMillis) {
		return null;
	}

	public static void clearTimeout(TimeoutHandler handler) {
	}

	public static void clearInterval(TimeoutHandler handler) {
	}

	public static <V> Array<V> $array(V... values) {
		return null;
	}

	public static <V> Map<V> $map() {
		return null;
	}

	public static String decodeURI(String uri) {
		return null;
	}

	public static String decodeURIComponent(String uri) {
		return null;
	}

	public static String encodeURI(String uri) {
		return null;
	}

	public static String encodeURIComponent(String uri) {
		return null;
	}

	public static String escape(String uri) {
		return null;
	}

	public static boolean isFinite(Object value) {
		return true;
	}

	public static boolean isNaN(Object value) {
		return true;
	}

	public static String unescape(String uri) {
		return null;
	}

}
