package org.stjs.generator.type;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class WildcardTypeWrapper3<T> {
	public T field;

	public T method(int p1, String p2) {
		return null;
	}

	public static <V> Gen<? extends WildcardTypeWrapper3<V>> get() {
		return null;
	}
}
