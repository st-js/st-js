package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions2b {
	public static void method(FunctionInterface2 func) {
		func.$invoke(0);
	}

	public static void main(String[] args) {
		method(new FunctionInterface2() {
			@SuppressWarnings("unused")
			private int test = 2;

			@Override
			public void $invoke(int arg) {
				arg = arg + 1;
			}

			@Override
			public void $invoke2(int arg2) {
				arg2 = arg2 + 1;
			}
		});
	}
}
