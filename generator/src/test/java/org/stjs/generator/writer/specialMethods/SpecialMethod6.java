package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod6 {
	@Template("array")
	public static Object $array(Object... arguments) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $array(1, 2);
	}
}
