package org.stjs.generator.writer.innerTypes;

public class InnerTypes22 {

	public interface Inner {
		int getValue();
	}

	public static int main(String[] args) {
		Inner obj = new Inner() {
			private int privateMethod() {
				return 5;
			}

			@Override
			public int getValue() {
				return privateMethod();
			}
		};
		return obj.getValue();
	}
}
