package org.stjs.generator.writer.specialMethods;

import static org.stjs.javascript.JSCollections.$map;

import org.stjs.javascript.Map;

public class SpecialMethod3 {

	public void method() {
		Map<String, Integer> map = $map();
		map.$put("3", 4);
	}
}
