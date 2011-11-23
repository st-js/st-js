package org.stjs.generator.scope.inner;

import org.stjs.javascript.functions.Callback2;

abstract public class AnonymousInnerClass3 {
	abstract void abstractMethod();

	public void method() {
		new Callback2<Object, Object>() {
			@Override
			public void $invoke(Object o1, Object o2) {
			}
		};
	}
}
