package org.stjs.generator.writer.inlineObjects;

public class InlineObjects6 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		BridgePojo o = new BridgePojo(){{
			x("hello");
			x(12);
		}};
	}
}
