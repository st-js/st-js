package org.stjs.javascript;

import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.Template;
import org.stjs.javascript.functions.Function1;

/**
 * <p>JSStringAdapterBase class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
@Adapter
public class JSStringAdapterBase {
	/**
	 * <p>match.</p>
	 *
	 * @param applyTo a {@link java.lang.String} object.
	 * @param re a {@link org.stjs.javascript.RegExp} object.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	@Template("adapter")
	public native static Array<String> match(String applyTo, RegExp re);

	/**
	 * <p>split.</p>
	 *
	 * @param applyTo a {@link java.lang.String} object.
	 * @param re a {@link java.lang.String} object.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	@Template("adapter")
	public native static Array<String> split(String applyTo, String re);

	/**
	 * <p>split.</p>
	 *
	 * @param applyTo a {@link java.lang.String} object.
	 * @param re a {@link java.lang.String} object.
	 * @param limit a int.
	 * @return a {@link org.stjs.javascript.Array} object.
	 */
	@Template("adapter")
	public native static Array<String> split(String applyTo, String re, int limit);

	/**
	 * <p>replace.</p>
	 *
	 * @param applyTo a {@link java.lang.String} object.
	 * @param re a {@link org.stjs.javascript.RegExp} object.
	 * @param repl a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	@Template("adapter")
	public native static String replace(String applyTo, RegExp re, String repl);

	/**
	 * <p>replace.</p>
	 *
	 * @param applyTo a {@link java.lang.String} object.
	 * @param re a {@link org.stjs.javascript.RegExp} object.
	 * @param replaceFunction a {@link org.stjs.javascript.functions.Function1} object.
	 * @return a {@link java.lang.String} object.
	 */
	@Template("adapter")
	public native static String replace(String applyTo, RegExp re, Function1<String, String> replaceFunction);

	/**
	 * <p>charCodeAt.</p>
	 *
	 * @param applyTo a {@link java.lang.String} object.
	 * @param x a int.
	 * @return a int.
	 */
	@Template("adapter")
	public native static int charCodeAt(String applyTo, int x);

	/**
	 * <p>fromCharCode.</p>
	 *
	 * @param applyTo a {@link java.lang.Class} object.
	 * @param codes a int.
	 * @return a {@link java.lang.String} object.
	 */
	@Template("adapter")
	public native static String fromCharCode(Class<? extends String> applyTo, int... codes);
}
