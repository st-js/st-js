package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions1 {
	public static void method(FunctionInterface func) {

	}

	public static void main(String[] args) {
		method(new FunctionInterface() {
			@Override
			public void $invoke(int arg) {
				arg = arg + 1;
			}
		});
	}
}
