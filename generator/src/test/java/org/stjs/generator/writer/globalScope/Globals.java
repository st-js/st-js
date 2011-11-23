package org.stjs.generator.writer.globalScope;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class Globals {
	public static int method() {
		return 0;
	}

	public static String field;

	public void instanceMethod() {

	}

	public String instanceField;

	public static Globals global = new Globals();
}
