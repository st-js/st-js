package org.stjs.generator.scope.checks;

public class Checks6 {
	public int method() {
		return 0;
	}

	public static class Inner extends Checks6 {
		public int method(int n) {
			return 0;
		}
	}
}
