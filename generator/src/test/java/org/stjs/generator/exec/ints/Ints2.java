package org.stjs.generator.exec.ints;

public class Ints2 {
	public long method(long a) {
		return a;
	}

	public static long main(String[] args) {
		return new Ints2().method((long) 2.3);
	}
}
