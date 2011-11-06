package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions4 {
	public static void method(FunctionInterface func) {

	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		FunctionInterface func = new FunctionInterface() {
			@Override
			public void $invoke(int arg) {
				arg = arg + 1;
			}
		};
	}
}
