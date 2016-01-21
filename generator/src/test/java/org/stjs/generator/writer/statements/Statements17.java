package org.stjs.generator.writer.statements;

public class Statements17 {

	public int method() {
		int result = 0;
		synchronized(this) {
			result = result + 999;
			for (int i = 0; i < 10; ++i) {
				result = result + i;
			}
		}
		return result;
	}
}
