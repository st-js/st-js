package org.stjs.generator.writer.inlineObjects;

public class InlineObjects2 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Pojo o = new Pojo() {
			{
				a = 1;
				b = "x";
				int x = 3;
			}
		};
	}
}
