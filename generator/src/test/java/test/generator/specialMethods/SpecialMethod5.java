package test.generator.specialMethods;

public class SpecialMethod5 {
	public static Object $map(Object k, Object v) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $map("key", 1);
	}
}
