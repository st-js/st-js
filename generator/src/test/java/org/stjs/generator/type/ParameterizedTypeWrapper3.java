package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class ParameterizedTypeWrapper3<T, V> {
	public T field;
	public V field2;

	public T method(int p1, String p2) {
		return null;
	}

	public static <V> ParameterizedTypeWrapper3<String, V> get() {
		return null;
	}
}
