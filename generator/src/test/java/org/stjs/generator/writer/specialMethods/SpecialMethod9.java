package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod9 {
	@Template("or")
	public static Object $or(Object... arguments) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $or(3, 4);
	}
}
