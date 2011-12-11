package org.stjs.generator.exec.json;

import static org.stjs.javascript.Global.stjs;

public class Json6 {

	public static Object main(String[] args) {
		return stjs.parseJSON("{\"a\":1, \"children\":[{\"i\":2}], \"child\":{\"i\":3}}", Class6.class);
	}
}
