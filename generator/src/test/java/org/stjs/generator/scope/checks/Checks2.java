package org.stjs.generator.scope.checks;

public class Checks2 {
	@SuppressWarnings("unused")
	private int field;

	public static class Inner extends Checks2 {
		public int field;
	}
}
