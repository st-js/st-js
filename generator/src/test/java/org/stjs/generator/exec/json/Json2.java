package org.stjs.generator.exec.json;

import static org.stjs.javascript.Global.stjs;

public class Json2 {

	public static Object main(String[] args) {
		return stjs.parseJSON("{\"map\":{\"key\":{\"a\":1}}}", Class2.class);
	}
}
