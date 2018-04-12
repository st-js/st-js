package org.stjs.generator.deps;

import static org.stjs.javascript.JSCollections.$array;
import static org.stjs.javascript.JSGlobal.JSON;

public class Dep5 {
	public void method(Object obj) {
		String ok = JSON.stringify($array(2));
	}
}
