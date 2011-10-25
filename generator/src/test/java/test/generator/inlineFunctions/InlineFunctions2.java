package test.generator.inlineFunctions;

public class InlineFunctions2 {
	public static void method(FunctionInterface func) {
		func.run(0);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("Class:" + Class.forName("test.generator.inlineFunctions.InlineFunctions2$1"));
		method(new FunctionInterface() {
			@SuppressWarnings("unused")
			private int test = 2;

			@Override
			public void run(int arg) {
				arg = arg + 1;
			}
		});
	}
}
