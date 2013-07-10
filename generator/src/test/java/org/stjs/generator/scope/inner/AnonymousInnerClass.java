package org.stjs.generator.scope.inner;

@SuppressWarnings("unused")
public class AnonymousInnerClass {

	private static MyClass m2 = new MyClass(2) {};

	public AnonymousInnerClass() {
		MyClass m3 = new MyClass(3) {

		};

		method(new MyClass(4) {
			@Override
			public void run() {

			}
		}).method2(new MyClass2(5) {
			public void run() {

			}
		});
		new InnerClass();
	}

	private AnonymousInnerClass method(MyClass myClass) {
		return this;
	}

	private AnonymousInnerClass method2(MyClass2 myClass) {
		return this;
	}

	private static MyClass m7 = new MyClass(7) {};

	public static class InnerClass {
		private static MyClass2 m8 = new MyClass2(8) {};

	}

}
