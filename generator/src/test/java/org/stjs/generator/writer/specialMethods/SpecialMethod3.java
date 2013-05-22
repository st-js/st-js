package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod3 {
	@Template("put")
	public void $put(String x, Integer y) {

	}

	public void method() {
		this.$put("3", 4);
	}
}
