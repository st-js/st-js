package org.stjs.generator.writer.operators;

public class Operator1 {
	public int test(int x) {
		return 0;
	}

	public int test2(int x) {
		int y = 1;
		return test(x ^ y);
	}
}
