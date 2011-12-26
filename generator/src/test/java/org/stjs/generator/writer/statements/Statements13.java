package org.stjs.generator.writer.statements;

import static org.stjs.javascript.Global.console;
import static org.stjs.javascript.JSCollections.$map;

import org.stjs.javascript.Map;

public class Statements13 {

	public void method() {
		Map<String, Integer> a = $map();
		for (String i : a) {
			int x = a.$get(i);
			console.info(x);
		}

	}

}
