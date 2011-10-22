package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

public class ParameterizedTypeWrapper2 {
	@GlobalScope
	public static class InnerType<T> {
		public T field;

		public T method(int p1, String p2) {
			return null;
		}
	}

	public static ParameterizedTypeWrapper2.InnerType<String> get;
}
