package org.stjs.generator.exec.inheritance;

import org.stjs.generator.exec.inheritance.Inheritance.B;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Inheritance1 {
	public static int main(String[] args) {
		B b = new B();
		int result =  b.method2(1);

		$js("console.log(result)");
		return 1;
	}
}
