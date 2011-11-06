package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions3 {
	public static void method(FunctionInterface2 func) {

	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		FunctionInterface2 func = new FunctionInterface2() {
			@Override
			public void $invoke(int arg) {
				arg = arg + 1;
			}

			@Override
			public void $invoke2(int arg2) {
				arg2 = arg2 + 1;
			}
		};
	}
}
