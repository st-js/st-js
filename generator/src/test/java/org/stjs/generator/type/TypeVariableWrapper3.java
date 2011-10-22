package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class TypeVariableWrapper3<T> {
	public T field;

	public T method(int p1, String p2) {
		return null;
	}

	public static <T extends TypeVariableWrapper3<V>, V> T get() {
		return null;
	}
}
