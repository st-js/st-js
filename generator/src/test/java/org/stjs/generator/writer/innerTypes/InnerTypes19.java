package org.stjs.generator.writer.innerTypes;

@SuppressWarnings("unused")
public class InnerTypes19 {

	public static void main(String[] args) {

	}

	class Inner {
		public Object doSomething() {
			return new Object() {
				public void doSomethingElse() {
					int i = 0;
				}
			};
		}
	}
}
