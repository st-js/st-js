package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class ParameterizedTypeWrapper1<T> {
	public T field;

	public T method(int p1, String p2) {
		return null;
	}

	public static ParameterizedTypeWrapper1<String> get;
}
