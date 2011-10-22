package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

public class TypeVariableWrapper2 {
	@GlobalScope
	public static class InnerType {
		public String field;

		public String method(int p1, String p2) {
			return null;
		}
	}

	public static <T extends TypeVariableWrapper2.InnerType> T get() {
		return null;
	}
}
