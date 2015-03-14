package org.stjs.generator.exec.ints;

public class Ints6 {
	public int method() {
		int a = 500;
		int b = 200;
		a /= b;
		return a;
	}

	public static int main(String[] args) {
		return new Ints6().method();
	}
}
