package org.stjs.generator.exec.statements;

import org.stjs.javascript.JSGlobal;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Statements1 {
	public static int main(String[] args) {
		int result = JSGlobal.typeof("xxx") == "number" ? 1 : 0;
		$js("console.log(result)");
		return 1;
	}
}
