package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions1b {
	public static void method(FunctionInterface3 func) {

	}

	public static void main(String[] args) {
		method(new FunctionInterface3() {
			@Override
			public void method(int arg) {
				arg = arg + 1;
			}
		});
	}
}
