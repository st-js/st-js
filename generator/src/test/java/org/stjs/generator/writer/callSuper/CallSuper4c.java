package org.stjs.generator.writer.callSuper;

public class CallSuper4c {
	public void instanceMethod2(final String arg) {
		new SuperClass3() {
			public void someMethod() {
				super.instanceMethod(arg);
			}
		};
	}
}
