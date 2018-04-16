package org.stjs.generator.writer.methods;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

import static org.stjs.javascript.JSCollections.$castArray;

public class Methods18<T extends Methods18<?>> {

	public T parent() {
		return null;
	}

	public Integer someArray(Array<Integer> arr) {
		return arr.$length();
	}

	public Integer somePrimitiveArray(Integer[] arr) {
		return $castArray(arr).$length();
	}

	public String someMap(Map<String, Object> a) {
		return "";
	}

	public <R extends String> R genericMethod(R someType, T thisClass) {
		return someType;
	}

	public Double main(String[] args) {
		Methods18<?> m = new Methods18();
		m.parent().parent();

		return 2.0;
	}
}
