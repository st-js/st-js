package org.stjs.generator.writer.fields;

import org.stjs.javascript.annotation.Template;

public class Fields23 {
	@Template("gproperty")
	public int field;

	public int method() {
		return field /= 2;
	}

}
