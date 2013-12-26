package org.stjs.generator.writer.statements;

import static org.stjs.javascript.JSCollections.$map;
import static org.stjs.javascript.JSGlobal.parseInt;

import org.stjs.javascript.Map;

public class Statements13 {

	public void method() {
		Map<String, Integer> a = $map();
		for (String i : a) {
			int x = a.$get(i);
			parseInt(x);
		}

	}

}
