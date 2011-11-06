package org.stjs.generator.writer.inlineFunctions;

public class InlineFunctions5 {
	public static interface I {
		public void $invoke();
	}

	abstract public static class C implements I {

	}

	public static void method(I param) {
	}

	public static void main(String[] args) {
		method(new C() {
			public void $invoke() {
			}
		});
	}
}
