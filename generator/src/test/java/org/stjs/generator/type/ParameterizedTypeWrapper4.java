package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class ParameterizedTypeWrapper4<T> {
	public T field;

	public T method(int p1, String p2) {
		return null;
	}

	public static class InnerType extends ParameterizedTypeWrapper4<String> {

	}

	public static InnerType get;
}
