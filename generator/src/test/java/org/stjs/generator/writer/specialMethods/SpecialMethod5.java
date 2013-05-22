package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod5 {
	@Template("map")
	public static Object $map(Object k, Object v) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $map("key", 1);
	}
}
