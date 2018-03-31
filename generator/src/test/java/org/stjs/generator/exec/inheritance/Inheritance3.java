package org.stjs.generator.exec.inheritance;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Inheritance3 extends MySuperClass {
	public static int main(String[] args) {
		Inheritance3 x = new Inheritance3();
		int result = (x instanceof MySuperClass) ? 1 : 0;

		$js("console.log(result)");
		return 1;
	}
}
