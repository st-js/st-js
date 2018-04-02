package org.stjs.generator.writer.globalScope;

import org.stjs.javascript.annotation.GlobalScope;

@GlobalScope
public class Globals {
	public static int method() {
		return 0;
	}

	public static final String hey = "you";

	public static String field;

	public static int one, two;

	public static void main(String[] args) {
		method();
	}

	static {
		int n = method();
	}
}
