package org.stjs.generator.exec.statements;

import org.stjs.javascript.JSObjectAdapter;

import static org.stjs.javascript.JSObjectAdapter.$js;

public class Statements2 {

	public static int main(String[] args) {
		int result = 2;
		Statements2 instance = new Statements2();
		if(instance.getClass() == Statements2.class){
			if(instance.getClass() == JSObjectAdapter.$constructor(instance)){
				result = 0;
			} else {
				result = 1;
			}
		}

		$js("console.log(result)");
		return 1;
	}

}
