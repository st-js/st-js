package org.stjs.generator.exec.ints;

public class Ints1 {
	public int method(int a) {
		return a;
	}

	public static int main(String[] args) {
		return new Ints1().method((int) 2.3);
	}
}
