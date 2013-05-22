package org.stjs.javascript.stjs;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Callback3;

/**
 * 
 * This is a collection of assert methods that are implemented in the stjs.js javascript file.
 * 
 * @author acraciun
 */
@GlobalScope
@SuppressWarnings("unused")
public class STJSAssert {
	@Template("assert")
	public static void assertArgEquals(Object expectedValue, Object testValue) {
		throw new UnsupportedOperationException();
	}

	@Template("assert")
	public static void assertArgNotNull(Object testValue) {
		throw new UnsupportedOperationException();
	}

	@Template("assert")
	public static void assertArgTrue(boolean condition) {
		throw new UnsupportedOperationException();
	}

	@Template("assert")
	public static void assertStateEquals(Object expectedValue, Object testValue) {
		throw new UnsupportedOperationException();
	}

	@Template("assert")
	public static void assertStateNotNull(Object testValue) {
		throw new UnsupportedOperationException();
	}

	@Template("assert")
	public static void assertStateTrue(boolean condition) {
		throw new UnsupportedOperationException();
	}

	@Template("assert")
	public static void setAssertHandler(Callback3<String, String, String> message) {
		throw new UnsupportedOperationException();
	}

}
