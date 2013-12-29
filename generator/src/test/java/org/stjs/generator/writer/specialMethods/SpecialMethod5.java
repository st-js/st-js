package org.stjs.generator.writer.specialMethods;

import static org.stjs.javascript.JSCollections.$map;

public class SpecialMethod5 {

	public void method() {
		@SuppressWarnings("unused")
		Object test = $map("key", 1);
	}
}
