package org.stjs.generator.writer.namespace;

import org.stjs.javascript.annotation.Namespace;

@Namespace("a.b")
public class Namespace2 {
	public class Child extends Namespace2 {
		public Child() {
			// Do something else than calling super() to force generation of the method;
			return;
		}
	}
}
