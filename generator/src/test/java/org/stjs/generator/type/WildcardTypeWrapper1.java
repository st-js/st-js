package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class WildcardTypeWrapper1 {
	public String field;

	public String method(int p1, String p2) {
		return null;
	}

	public static Gen<? extends WildcardTypeWrapper1> get() {
		return null;
	}
}
