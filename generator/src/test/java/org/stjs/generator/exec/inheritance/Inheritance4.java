package org.stjs.generator.exec.inheritance;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Inheritance4 implements MyInterface {
	public static int main(String[] args) {
		Inheritance4 x = new Inheritance4();
		int result =  (x instanceof MyInterface) ? 1 : 0;

		$js("console.log(result)");
		return 1;
	}
}
