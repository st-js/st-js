package org.stjs.generator.exec.json;

import static org.stjs.javascript.Global.stjs;

public class Json5 {

	public static Object main(String[] args) {
		return stjs.parseJSON("{\"e\":\"b\"}", Class5.class);
	}
}
