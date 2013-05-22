package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod12 {
	@Template("get")
	public Long $get(String obj, String prop) {
		return 1l;
	}

	public void method() {
		String obj = "";
		this.$get(obj, "a");
	}
}
