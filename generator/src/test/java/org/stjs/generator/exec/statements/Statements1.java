package org.stjs.generator.exec.statements;

import org.stjs.javascript.JSGlobal;

public class Statements1 {
	public static int main(String[] args) {
		return JSGlobal.typeof("xxx").equals("number") ? 1 : 0;
	}
}
