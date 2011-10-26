package org.stjs.generator.writer.names;

public class Names4 {
	public static <T> T genericMethod(T arg) {
		return arg;
	}

	public int method() {
		return genericMethod(2);
	}
}
