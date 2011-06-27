package org.stjs.generator;

import static java.lang.String.format;

public class PreConditions {

	public static void checkState(boolean check, String message, Object... args) {
		if (!check) {
			throw new IllegalArgumentException(format(message, args));
		}
	}
	
	public static void notNull(Object obj, String message, Object... args) {
		if (obj == null) {
			throw new NullPointerException(format(message, args));
		}
	}
}
