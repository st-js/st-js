package test.generator.names;

public class Names7 {
	public static int staticMethod() {
		return 2;
	}

	public int method() {
		return Names7.staticMethod();
	}
}
