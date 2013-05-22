package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod5a {
	@Template("map")
	public static Object $map(Object k, Object v) {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		Object test = $map(2, 1);
	}
}
