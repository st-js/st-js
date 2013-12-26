package org.stjs.generator.exec.json;

import static org.stjs.javascript.JSGlobal.stjs;

public class Json3 {

	public static Object main(String[] args) {
		return stjs.parseJSON("{\"map\":{\"key1\":{\"key2\":{\"a\":1}}}}", Class3.class);
	}
}
