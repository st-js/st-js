package org.stjs.generator.scope.checks;

public class Checks4 {
	@SuppressWarnings("unused")
	private int method() {
		return 0;
	}

	public static class Inner extends Checks4 {
		public int method() {
			return 0;
		}
	}
}
