package test.generator.specialMethods;

public class SpecialMethod4 {
	public int $invoke(Object... args) {
		return 0;
	}

	public void method() {
		this.$invoke("3", 4);
	}
}
