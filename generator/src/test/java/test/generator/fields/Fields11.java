package test.generator.fields;

public class Fields11 {

	public static final String FIELD = "abc";

	public static class InnerType {
		@SuppressWarnings("unused")
		private void method() {
			String a = FIELD;
		}
	}
}
