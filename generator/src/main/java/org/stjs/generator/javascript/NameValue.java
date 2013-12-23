package org.stjs.generator.javascript;

public final class NameValue<JS> {
	private final CharSequence name;
	private final JS value;

	private NameValue(CharSequence name, JS value) {
		this.name = name;
		this.value = value;
	}

	public static <T> NameValue<T> of(CharSequence name, T value) {
		return new NameValue<T>(name, value);
	}

	public JS getValue() {
		return value;
	}

	public CharSequence getName() {
		return name;
	}

}
