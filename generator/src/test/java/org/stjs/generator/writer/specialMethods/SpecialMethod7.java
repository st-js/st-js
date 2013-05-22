package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod7 {
	@Template("toProperty")
	public int $length() {
		return 0;
	}

	public void method() {
		@SuppressWarnings("unused")
		int x = this.$length();
	}
}
