package org.stjs.javascript.stjs;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.shared.functions.Callback3;

/**
 * 
 * This is a collection of assert methods that are implemented in the stjs.js javascript file.
 * 
 * @author acraciun
 */
@GlobalScope
@SuppressWarnings("unused")
public class STJSAssert {
	public static void assertArgEquals(Object expectedValue, Object testValue) {
		throw new UnsupportedOperationException();
	}

	public static void assertArgNotNull(Object testValue) {
		throw new UnsupportedOperationException();
	}

	public static void assertArgTrue(boolean condition) {
		throw new UnsupportedOperationException();
	}

	public static void assertStateEquals(Object expectedValue, Object testValue) {
		throw new UnsupportedOperationException();
	}

	public static void assertStateNotNull(Object testValue) {
		throw new UnsupportedOperationException();
	}

	public static void assertStateTrue(boolean condition) {
		throw new UnsupportedOperationException();
	}

	public static void setAssertHandler(Callback3<String, String, String> message) {
		throw new UnsupportedOperationException();
	}

}
