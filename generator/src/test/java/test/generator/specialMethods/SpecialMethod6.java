package test.generator.specialMethods;

public class SpecialMethod6 {
	public static Object $array(Object... v) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $array(1, 2);
	}
}
