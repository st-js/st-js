package org.stjs.generator.writer.specialMethods;

import org.stjs.javascript.annotation.Template;

public class SpecialMethod24 {

	@Template("prefix")
	private String _prefix() {
		return "a";
	}

	@SuppressWarnings("unused")
	public void method() {
		String n = _prefix();
	}
}
