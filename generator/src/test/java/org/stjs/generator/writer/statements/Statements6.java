package org.stjs.generator.writer.statements;

import static org.stjs.javascript.JSCollections.$castArray;

import org.stjs.javascript.Array;

public class Statements6 {
	public void method(Array<Integer> x) {

	}

	public void main() {
		method($castArray(new Integer[] { 1, 2, 3 }));
	}
}
