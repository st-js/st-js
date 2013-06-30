package org.stjs.generator.writer.innerTypes;

import org.stjs.javascript.functions.Callback0;
import org.stjs.javascript.functions.Function0;

public class InnerTypes13 {

	private final Button plusBtn;

	// private final Button minusBtn;

	public InnerTypes13(String id, String name, String labelText, final int decimals) {
		plusBtn = new Button("+", new Callback0() {

			@Override
			public void $invoke() {

			}
		});

		method(new Function0<Boolean>() {
			@Override
			public Boolean $invoke() {
				return true;
			}
		});

	}

	public static class Button {

		public Button(Object content, final Callback0 handler) {
		}
	}

	public static void method(Function0<Boolean> f) {
	}
}