package org.stjs.generator.writer.namespace;

import org.stjs.javascript.annotation.Namespace;

@Namespace("a.b")
public class Namespace5 {
	public static void staticMethod() {

	}

	public void method() {
		staticMethod();
	}

}
