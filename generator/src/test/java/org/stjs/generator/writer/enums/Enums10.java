package org.stjs.generator.writer.enums;

/**
 * (c) Swissquote 05.04.18
 *
 * @author sgoetz
 */
public class Enums10 {
	public enum Value {
		a, b, c;
	}

	public void main() {
		@SuppressWarnings("unused")
		String x = Value.a.name();
	}
}
