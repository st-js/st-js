package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod22 {
	@Template("none")
	public String $get(int index) {
		return "";
	}

	@SuppressWarnings("unused")
	public void method() {
		SpecialMethod22 m = new SpecialMethod22();
		String n = m.$get(0);
	}
}
