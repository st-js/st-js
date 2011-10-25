package test.generator;

import test.generator.inlineFunctions.FunctionInterface;

public class MyTestBean {
	private static int pollingInterval = 7; // in seconds

	private static void m(double d, int n) {

	}

	public static void main(String[] args) {
		m(1, 0);
		int n = new FunctionInterface() {
			@SuppressWarnings("unused")
			public int test = 2;

			@Override
			public void run(int arg) {
				arg = arg + 1;
			}
		}.test;
	}

	// to fix in a test
	private int x;

	void mm() {
		int y = x;// here x is int
		String x = "hello";
		String k = x; // here x is string

	}
}
