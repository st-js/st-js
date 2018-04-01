package org.stjs.generator.plugin.java8.writer.interfaces;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Class2 implements Interface2 {
	public static int main(String[] args) {
		int result = new Class2().instanceMethod();
		$js("console.log(result)");
		return result;
	}
}
