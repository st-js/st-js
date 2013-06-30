package org.stjs.generator.writer.innerTypes;

public class InnerTypes21 {

	public interface Inner {
		int getValue();
	}

	public static int main(String[] args) {
		Inner obj = new Inner() {
			public int getValue() {
				return 5;
			}
		};
		return obj.getValue();
	}
}
