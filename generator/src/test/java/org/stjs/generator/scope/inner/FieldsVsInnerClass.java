package org.stjs.generator.scope.inner;

public class FieldsVsInnerClass {

	FieldsVsInnerClass MyInnerClass;

	public static void main(String[] args) {
		new FieldsVsInnerClass().doSth();
	}

	public void doSth() {
		MyInnerClass = new FieldsVsInnerClass();
		MyInnerClass.print();
		MyInnerClass2.print();
	}

	private void print() {

	}

	static class MyInnerClass {

		static void print() {

		}
	}

	static class MyInnerClass2 {

		static void print() {

		}
	}
}
