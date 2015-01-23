package org.stjs.generator.writer.inlineFunctions;

import org.stjs.javascript.functions.Callback0;

public class InlineFunctions7 {
	private void outerMethod() {
	}

	public void method() {
		new Callback0() {
			@Override
			public void $invoke() {
				outerMethod();
			}
		};
	}

}
