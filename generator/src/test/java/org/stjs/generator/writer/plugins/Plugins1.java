package org.stjs.generator.writer.plugins;

import org.stjs.javascript.annotation.UsePlugin;

@UsePlugin("contributor")
public class Plugins1 {
	int method(int a) {
		return a + 10;
	}
}
