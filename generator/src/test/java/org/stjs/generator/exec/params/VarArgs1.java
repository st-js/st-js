package org.stjs.generator.exec.params;

import static org.stjs.javascript.JSCollections.$castArray;

import org.stjs.javascript.Array;
import org.stjs.javascript.functions.Function2;

public class VarArgs1 {
	public static int add(int a, int b, Integer... other) {
		Array<Integer> otherArray = $castArray(other);
		return otherArray.reduce(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer $invoke(Integer r, Integer v) {
				return r + v;
			}
		}) + a + b;
	}

	public static int main(String[] args) {
		return add(1, 2, 3, 4);
	}
}
