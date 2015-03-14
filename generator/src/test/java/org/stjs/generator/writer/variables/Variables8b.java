package org.stjs.generator.writer.variables;

import static org.stjs.generator.writer.variables.GlobalVars.x;

import org.stjs.javascript.functions.Callback0;

public class Variables8b {
	@SuppressWarnings("unused")
	void m() {
		if (true) {
			new Callback0() {
				@Override
				public void $invoke() {
					int x = 0;
				}

			};
		}
		int y = x;
	}
}
