package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions2 {
	public static void method(FunctionInterface func) {
		func.$invoke(0);
	}

	public static void main(String[] args) {
		method(new FunctionInterface() {
			@SuppressWarnings("unused")
			private int test = 2;

			@Override
			public void $invoke(int arg) {
				arg = arg + 1;
			}
		});
	}
}
