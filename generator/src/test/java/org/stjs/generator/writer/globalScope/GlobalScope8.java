package org.stjs.generator.writer.globalScope;

import org.stjs.javascript.annotation.GlobalScope;

import static org.stjs.javascript.JSObjectAdapter.$js;

@GlobalScope
public class GlobalScope8 {
	public static int main(String[] args) {
		int result = 2;
		$js("console.log(result)");
		return result;
	}
}
