package org.stjs.javascript;

import org.stjs.javascript.jquery.GlobalJquery;
import org.stjs.javascript.jquery.JQuery;

public class Global {
	public static GlobalJquery $;

	/**
	 * jquery constructors
	 */
	public static JQuery $(String path) {
		// return new JQuery(path);
		return null;
	}

	public static JQuery $(Object path) {
		// return new JQuery(path);
		return null;
	}

	public static Object eval(String expr) {
		return null;
	}

	public static void alert(String expr) {
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
}
