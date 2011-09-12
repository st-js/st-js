package test.generator.names;

public class Names8 {
	public static int staticMethod() {
		return 2;
	}

	public int method() {
		return test.generator.names.Names8.staticMethod();
	}
}
