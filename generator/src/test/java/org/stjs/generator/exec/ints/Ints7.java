package org.stjs.generator.exec.ints;

public class Ints7 {
	public int method() {
		int a = 500;
		int b = 200;
		int c = 1;
		a /= b + c;
		return a;
	}

	public static int main(String[] args) {
		return new Ints7().method();
	}
}
