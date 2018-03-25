package org.stjs.generator.javascript;

/**
 * <p>NameValue class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public final class NameValue<JS> {
	private final CharSequence name;
	private final JS value;

	private NameValue(CharSequence name, JS value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * <p>of.</p>
	 *
	 * @param name a {@link java.lang.CharSequence} object.
	 * @param value a T object.
	 * @param <T> a T object.
	 * @return a {@link org.stjs.generator.javascript.NameValue} object.
	 */
	public static <T> NameValue<T> of(CharSequence name, T value) {
		return new NameValue<T>(name, value);
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a JS object.
	 */
	public JS getValue() {
		return value;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.CharSequence} object.
	 */
	public CharSequence getName() {
		return name;
	}

}
