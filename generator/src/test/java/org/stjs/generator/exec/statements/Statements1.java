package org.stjs.generator.exec.statements;

import org.stjs.javascript.Global;

public class Statements1 {
	public static int main(String[] args) {
		return Global.typeof("xxx").equals("number") ? 1 : 0;
	}
}
