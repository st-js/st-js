package test.generator.methods;

public class Methods12 {
	public static interface I {

	}

	public static class C implements I {

	}

	public static void method(I param) {
	}

	public static void main(String[] args) {
		C c = null;
		method(c);
	}
}
