package org.stjs.generator.exec.json;

import static org.stjs.javascript.JSGlobal.stjs;

import org.stjs.generator.exec.json.Class5.MyEnum;

public class Json5c {

	public static Object main(String[] args) {
		Class5 obj = new Class5();
		obj.e = MyEnum.b;
		obj.number = 2;

		obj.child = new Class5();
		obj.child.number = 4;
		return stjs.stringify(obj, Class5.class);
	}
}
