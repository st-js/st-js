package org.stjs.generator.writer.specialMethods;

import static org.stjs.javascript.JSCollections.$map;

import org.stjs.javascript.Map;

public class SpecialMethod12 {

	public void method() {
		Map<String, Object> obj = $map();
		obj.$get("a");
	}
}
