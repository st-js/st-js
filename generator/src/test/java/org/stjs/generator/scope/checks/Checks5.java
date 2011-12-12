package org.stjs.generator.scope.checks;

public class Checks5 {
	public int method() {
		return 0;
	}

	public static class Inner extends Checks5 {
		@Override
		public int method() {
			return 0;
		}
	}
}
