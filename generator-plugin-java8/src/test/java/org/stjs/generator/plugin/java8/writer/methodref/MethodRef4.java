package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Function1;

public class MethodRef4 {
	private final int n;

	public MethodRef4(int n) {
		this.n = n;
	}

	public int getN() {
		return n;
	}

	private static MethodRef4 calculate(Function1<Integer, MethodRef4> f, int n) {
		return f.$invoke(n);
	}

	public static int main(String[] args) {
		return calculate(MethodRef4::new, 1).getN();
	}
}
