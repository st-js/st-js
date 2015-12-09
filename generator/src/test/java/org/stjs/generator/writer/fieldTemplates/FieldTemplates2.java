package org.stjs.generator.writer.fieldTemplates;

import org.stjs.javascript.annotation.Template;

public class FieldTemplates2 {

	public static class Inner {
		@Template("path")
		public String field;
	}

	@Template("path(method)")
	public Inner inner;

	public String method(String param) {
		return "";
	}

	public void other() {
		String s = me().inner.field.substring(0, 2);
	}

	private FieldTemplates2 me() {
		return this;
	}
}
