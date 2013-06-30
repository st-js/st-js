package org.stjs.generator.writer.innerTypes;

@SuppressWarnings("unused")
public class InnerTypes18 {

	public static void main(String[] args) {
		Object o = new Object() {
			public void doSomething() {
				Object o2 = new Object() {
					public void doSomethingElse() {
						int i = 0;
					}
				};
			}
		};
	}

}
