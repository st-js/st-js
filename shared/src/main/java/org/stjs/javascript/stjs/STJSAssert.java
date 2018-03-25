package org.stjs.javascript.stjs;

import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Callback3;

/**
 * This is a collection of assert methods that are implemented in the stjs.js javascript file.
 *
 * @author acraciun
 * @version $Id: $Id
 */

@Namespace("stjs")

@SuppressWarnings("unused")
public class STJSAssert {
	/**
	 * <p>assertArgEquals.</p>
	 *
	 * @param expectedValue a {@link java.lang.Object} object.
	 * @param testValue a {@link java.lang.Object} object.
	 */
	@Template("assert")
	public native static void assertArgEquals(Object expectedValue, Object testValue);

	/**
	 * <p>assertArgNotNull.</p>
	 *
	 * @param testValue a {@link java.lang.Object} object.
	 */
	@Template("assert")
	public native static void assertArgNotNull(Object testValue);

	/**
	 * <p>assertArgTrue.</p>
	 *
	 * @param condition a boolean.
	 */
	@Template("assert")
	public native static void assertArgTrue(boolean condition);

	/**
	 * <p>assertStateEquals.</p>
	 *
	 * @param expectedValue a {@link java.lang.Object} object.
	 * @param testValue a {@link java.lang.Object} object.
	 */
	@Template("assert")
	public native static void assertStateEquals(Object expectedValue, Object testValue);

	/**
	 * <p>assertStateNotNull.</p>
	 *
	 * @param testValue a {@link java.lang.Object} object.
	 */
	@Template("assert")
	public native static void assertStateNotNull(Object testValue);

	/**
	 * <p>assertStateTrue.</p>
	 *
	 * @param condition a boolean.
	 */
	@Template("assert")
	public native static void assertStateTrue(boolean condition);

	/**
	 * <p>setAssertHandler.</p>
	 *
	 * @param message a {@link org.stjs.javascript.functions.Callback3} object.
	 */
	public native static void setAssertHandler(Callback3<String, String, String> message);

}
