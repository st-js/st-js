package org.stjs.generator.writer.specialMethods;

public class SpecialMethod5b {
	public static Object $map(Object k, Object v) {
		return 0;
	}

	public void method() {
		String a = "abc";
		@SuppressWarnings("unused")
		Object test = $map(a, 1);
	}
}
