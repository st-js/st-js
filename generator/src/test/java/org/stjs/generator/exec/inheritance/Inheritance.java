package org.stjs.generator.exec.inheritance;


public class Inheritance {
	public static class A {
		public int method1(int n) {
			return n;
		}

		public int method2(int n) {
			return method1(n + 1);
		}
	}

	public static class B extends A {
		@Override
		public int method1(int n) {
			return super.method1(n + 1);
		}

		@Override
		public int method2(int n) {
			return super.method2(n + 1);
		}
	}

	public static class C extends B {//
	}
}
