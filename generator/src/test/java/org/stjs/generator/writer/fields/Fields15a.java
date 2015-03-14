package org.stjs.generator.writer.fields;

import org.stjs.javascript.annotation.Template;

public class Fields15a {

	@Template("setter")
	public static String field;

	public void method(String n) {
		field = n;
	}

	public static void set(String field, Object value) {
	}
}
