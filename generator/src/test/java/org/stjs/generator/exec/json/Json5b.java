package org.stjs.generator.exec.json;

import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.javascript.JSGlobal.stjs;

import org.stjs.javascript.Map;

public class Json5b {

	public static Object main(String[] args) {
		Map<String, String> json = $map("e", "b");
		return stjs.typefy(json, Class5.class);
	}
}
