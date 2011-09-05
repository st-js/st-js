package test.generator.specialMethods;

public class SpecialMethod7 {
	public int $length() {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		int x = this.$length();
	}
}
