package org.stjs.generator.scope.checks;

public class Checks3 {
	@SuppressWarnings("unused")
	private int field;

	public static class Inner extends Checks3 {
		public int field() {
			return 0;
		}
	}
}
