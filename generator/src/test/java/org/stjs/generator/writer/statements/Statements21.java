package org.stjs.generator.writer.statements;

public class Statements21 {
	public interface MyInterface {

		public void method1();

		public void method2();

	}

	static {
		new MyInterface() {

			@Override
			public void method1() {
				//
			}

			@Override
			public void method2() {
				//
			}
		};
	}

}
