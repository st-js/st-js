package org.stjs.generator.plugin.java8.writer.methodref;

import org.stjs.javascript.functions.Function2;

public class MethodRef9{

	private static int calculate(Function2<? super Inc, Integer, Integer> f, Inc ref, int n) {
		return f.$invoke(ref, n);
	}

	public static int main(String[] args) {
		return calculate(Inc::inc2, new IncImpl(), 1);
	}

	public static class IncImpl implements Inc {
		public int inc2(int i) {
			return i + 2;
		}
	}

	public interface Inc {
		int inc2(int i);
	}
}
