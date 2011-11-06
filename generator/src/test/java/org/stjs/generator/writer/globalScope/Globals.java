package org.stjs.generator.writer.globalScope;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class Globals {
	public static void method() {

	}

	public static String field;

	public void instanceMethod() {

	}

	public String instanceField;

	public static Globals global = new Globals();
}
