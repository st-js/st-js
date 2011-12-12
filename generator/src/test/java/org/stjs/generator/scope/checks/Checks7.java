package org.stjs.generator.scope.checks;

abstract public class Checks7<T> {
	abstract protected int method(T t);

	public static class Inner extends Checks7<String> {
		@Override
		public int method(String n) {
			return 0;
		}
	}
}
