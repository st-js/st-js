package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod2 {
	@Template("set")
	public void $set(String x, Integer y) {

	}

	public void method() {
		this.$set("3", 4);
	}
}
