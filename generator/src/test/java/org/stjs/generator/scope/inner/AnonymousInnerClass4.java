package org.stjs.generator.scope.inner;

import org.stjs.javascript.functions.Callback2;

abstract public class AnonymousInnerClass4 {
	public void method() {
		InnerClass n = new InnerClass();
		new Callback2<Object, Object>() {
			@Override
			public void $invoke(Object o1, Object o2) {
			}
		};
	}

	private static class InnerClass {
	}
}
