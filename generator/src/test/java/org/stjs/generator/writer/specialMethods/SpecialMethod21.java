package org.stjs.generator.writer.specialMethods;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class SpecialMethod21 {
	public String a;

	@SuppressWarnings("unused")
	public void method() {
		SpecialMethod21 s = new SpecialMethod21();
		String x = $js("s.a");
	}
}
