package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

public class WildcardTypeWrapper2 {
	@GlobalScope
	public static class InnerType {
		public String field;

		public String method(int p1, String p2) {
			return null;
		}
	}

	public static Gen<? extends WildcardTypeWrapper2.InnerType> get() {
		return null;
	}
}
