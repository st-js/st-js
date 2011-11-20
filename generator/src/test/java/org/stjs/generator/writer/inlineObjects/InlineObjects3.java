package org.stjs.generator.writer.inlineObjects;

import org.stjs.javascript.functions.Callback0;

public class InlineObjects3 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Pojo o = new Pojo() {
			{
				a = 1;
				r = new Callback0() {
					@Override
					public void $invoke() {
						int x = 2;
					}
				};
			}
		};

	}
}
