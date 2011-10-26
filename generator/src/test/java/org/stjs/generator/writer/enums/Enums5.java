package org.stjs.generator.writer.enums;

public class Enums5 {
	public enum Value {
		a, b, c;
	}

	public void main() {
		@SuppressWarnings("unused")
		int x = Value.a.ordinal();
	}
}
