package org.stjs.generator.writer.statements;

import org.stjs.javascript.annotation.GlobalScope;

import static org.stjs.javascript.JSObjectAdapter.$js;

@GlobalScope
public class Statements16 {

	public static int a = 2;

	static {
		// after this line is executed, the value of "a" in the global scope
		// must still be 2
		int a = 3;
	}

	public static int main(String[] args) {
		int result = a;
		$js("console.log(result)");
		return result;
	}
}
