package org.stjs.generator.writer.names;

public class Names10 {

	public int method() {
		final Names10 that = this;
		new Runnable() {
			@Override
			public void run() {
				that.method();
			}
		};
		return 1;
	}
}
