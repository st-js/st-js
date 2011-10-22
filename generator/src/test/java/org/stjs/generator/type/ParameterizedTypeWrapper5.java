package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class ParameterizedTypeWrapper5<T> {
	public T field;

	public T method(int p1, String p2) {
		return null;
	}

	public static class InnerType<X, V> extends ParameterizedTypeWrapper5<V> {

	}

	public static InnerType<Integer, String> get;
}
