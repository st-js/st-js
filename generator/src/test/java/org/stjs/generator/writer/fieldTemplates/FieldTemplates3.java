package org.stjs.generator.writer.fieldTemplates;

import org.stjs.javascript.annotation.Template;

public class FieldTemplates3 {
	public static class Inner {
		@Template("path")
		public String field;
	}

	@Template("path")
	public Inner inner;

	public String method(String param) {
		return "";
	}

	public void other() {
		String s = this.inner.field;

	}
}
