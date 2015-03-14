package org.stjs.generator.exec.json;

import static org.stjs.javascript.JSGlobal.stjs;

public class Json4 {

	public static Object main(String[] args) {
		// yyyy-MM-dd HH:mm:ss
		return stjs.parseJSON("{\"date\":\"2011-12-21 18:56:00\"}", Class4.class);
	}
}
