package test.generator.innerTypes;

public class InnerTypes9 {
	public static class InnerType {
		public static int innerField;
	}

	public void method() {
		@SuppressWarnings("unused")
		int n = InnerTypes9.InnerType.innerField;
	}

}
