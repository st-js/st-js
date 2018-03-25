package org.stjs.javascript;

/**
 * <p>JSON class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public final class JSON {
	private JSON() {
		// forbid the creation using this type
	}

	/**
	 * <p>parse.</p>
	 *
	 * @param text a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public native Object parse  (String text);

	/**
	 * <p>stringify.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	public native String stringify  (Object obj);
}
