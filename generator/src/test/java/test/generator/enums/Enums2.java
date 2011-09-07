package test.generator.enums;

public class Enums2 {
	public enum Value {
		a, b, c;
	}

	public void main() {
		@SuppressWarnings("unused")
		Value x = Value.a;
	}
}