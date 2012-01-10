package org.stjs.generator.writer.namespace;

import org.stjs.javascript.annotation.Namespace;

@Namespace("a.b")
public class Namespace4 {
	public void method() {

	}

	public static class Child extends Namespace4 {
		@Override
		public void method() {
			super.method();
		}
	}
}
