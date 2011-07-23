package test;

public class Enums1 {
	public enum Value {
		value1, value2;
	}

	public void main() {
		String s = "x";
		if (s.equals("y")) {

		}
		Value x = Value.value1;
		switch (x) {
		case value1:
			break;
		case value2:
			break;
		}
	}
}
