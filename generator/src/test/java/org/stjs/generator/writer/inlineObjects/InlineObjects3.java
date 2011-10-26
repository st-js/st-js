package org.stjs.generator.writer.inlineObjects;

public class InlineObjects3 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Pojo o = new Pojo() {
			{
				a = 1;
				r = new Runnable() {
					@Override
					public void run() {
						int x = 2;
					}
				};
			}
		};
	}
}
