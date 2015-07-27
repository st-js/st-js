package org.stjs.generator.writer.inlineObjects;

import org.stjs.javascript.JSObjectAdapter;

public class InlineObjects8 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final String str = "foo";

		BridgePojo o = new BridgePojo(){{
			JSObjectAdapter.$constructor(str);
		}};
	}
}
