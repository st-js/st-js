package org.stjs.generator.exec.inheritance;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Inheritance5 implements MyInterface {
	public static int main(String[] args) {
		Inheritance5 x = new Inheritance5();
		int result =  (x instanceof MySuperInterface) ? 1 : 0;

		$js("console.log(result)");
		return 1;
	}
}
