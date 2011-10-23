package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class GenericArrayTypeWrapper1 {
	public String field;

	public String method(int p1, String p2) {
		return null;
	}

	public static <T extends GenericArrayTypeWrapper1> T[] get() {
		return null;
	}
}
