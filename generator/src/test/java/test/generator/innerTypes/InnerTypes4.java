package test.generator.innerTypes;

public class InnerTypes4 {
	class InnerType {
		public int innerField;
	}

	public int method() {
		return new InnerTypes4.InnerType().innerField;
	}
}
