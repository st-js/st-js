package org.stjs.javascript;

import org.stjs.javascript.jquery.GlobalJQuery;
import org.stjs.javascript.jquery.JQueryAndPlugins;

public class Global {
	public static GlobalJQuery $;

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

	public static double parseFloat(Object expr) {
		return 0;
	}

	public static int parseInt(Object expr) {
		return 0;
	}

	public static <T> T nvl(T value, T valueIfNull) {
		return value;
	}

	public static <V> Array<V> $array() {
		return null;
	}

	public static <V> Map<V> $map() {
		return null;
	}

}
