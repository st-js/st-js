package org.stjs.generator.exec.json;

import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.javascript.JSGlobal.stjs;

import org.stjs.javascript.Map;

public class Json4b {

	public static Object main(String[] args) {
		// yyyy-MM-dd HH:mm:ss
		Map<String, String> json = $map("date", "2011-12-21 18:56:00");
		return stjs.typefy(json, Class4.class);
	}
}
