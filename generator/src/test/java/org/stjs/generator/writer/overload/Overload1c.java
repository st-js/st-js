package org.stjs.generator.writer.overload;

import org.stjs.javascript.annotation.Native;

public class Overload1c {
	@Native
	public Overload1c() {
		// this should not be generated
		int n = 10;
	}
}
