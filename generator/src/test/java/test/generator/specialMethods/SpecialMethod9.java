package test.generator.specialMethods;

public class SpecialMethod9 {
	public static Object $or(Object... v) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $or(3, 4);
	}
}
