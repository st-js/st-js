package test.generator.enums;

public enum Enums4 {
	a(1), b(2), c(3);

	private final int value;

	private Enums4(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

}
