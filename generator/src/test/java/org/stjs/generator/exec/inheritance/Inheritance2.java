package org.stjs.generator.exec.inheritance;

import org.stjs.generator.exec.inheritance.Inheritance.C;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Inheritance2 {
	public static int main(String[] args) {
		C c = new C();
		int result = c.method1(1);

		$js("console.log(result)");
		return 1;
	}
}
