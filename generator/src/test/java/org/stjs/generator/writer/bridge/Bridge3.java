package org.stjs.generator.writer.bridge;

import org.stjs.javascript.annotation.STJSBridge;

import static org.stjs.javascript.JSObjectAdapter.$js;

@STJSBridge
public class Bridge3 {
	public static int main(String[] args) {
		int result = 1;
		$js("console.log(result)");
		return 1;
	}
}
