package org.stjs.javascript.stjs;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Callback3;

/**
 * This is a collection of assert methods that are implemented in the stjs.js javascript file.
 * @author acraciun
 */
@GlobalScope
@SuppressWarnings("unused")
public class STJSAssert {
	@Template("assert")
	public native static void assertArgEquals(Object expectedValue, Object testValue);

	@Template("assert")
	public native static void assertArgNotNull(Object testValue);

	@Template("assert")
	public native static void assertArgTrue(boolean condition);

	@Template("assert")
	public native static void assertStateEquals(Object expectedValue, Object testValue);

	@Template("assert")
	public native static void assertStateNotNull(Object testValue);

	@Template("assert")
	public native static void assertStateTrue(boolean condition);

	public native static void setAssertHandler(Callback3<String, String, String> message);

}
