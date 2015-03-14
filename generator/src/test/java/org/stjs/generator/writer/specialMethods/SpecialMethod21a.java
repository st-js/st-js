package org.stjs.generator.writer.specialMethods;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class SpecialMethod21a {
	public String a;

	@SuppressWarnings("unused")
	public String method() {
		SpecialMethod21a s = new SpecialMethod21a();
		if (a == null) {
			$js("return s.a");
		}
		return a;
	}
}
