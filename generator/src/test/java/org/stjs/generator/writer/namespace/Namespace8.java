package org.stjs.generator.writer.namespace;

import org.stjs.javascript.annotation.Namespace;

@Namespace("a.b")
abstract public class Namespace8 {
	abstract public int method();

	@SuppressWarnings("unused")
	public static void staticMethod() {
		Namespace8 n = new Namespace8() {
			@Override
			public int method() {
				return 1;
			}
		};
	}
}
