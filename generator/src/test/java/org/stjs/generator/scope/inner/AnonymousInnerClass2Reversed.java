package org.stjs.generator.scope.inner;

import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;

public class AnonymousInnerClass2Reversed {
	public void method() {
		// this is to make sure the 2nd level anon type is found also. the first class should be resolved correctly
		new Callback2<Object, Object>() {
			@Override
			public void $invoke(Object o1, Object o2) {
			}
		};

		new Callback0() {
			@Override
			public void $invoke() {
				new Callback1<Object>() {
					@Override
					public void $invoke(Object o) {
					}
				};
			}
		};

	}
}
