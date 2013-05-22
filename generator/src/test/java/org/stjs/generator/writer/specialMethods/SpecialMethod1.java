package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod1 {
	@Template("get")
	public Long $get(String x) {
		return 1l;
	}

	public void method() {
		this.$get("3");
	}
}
