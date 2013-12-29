package org.stjs.generator.writer.specialMethods;

import static org.stjs.javascript.JSCollections.$map;

public class SpecialMethod5b {

	public void method() {
		String a = "abc";
		@SuppressWarnings("unused")
		Object test = $map(a, 1);
	}
}
