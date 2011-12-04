package org.stjs.generator.writer.specialMethods;

import org.stjs.generator.writer.inlineFunctions.FunctionInterface;

public class SpecialMethod4 {

	public void method() {
		FunctionInterface f = new FunctionInterface() {
			@Override
			public void $invoke(int arg) {
			}
		};
		f.$invoke(4);
	}
}
