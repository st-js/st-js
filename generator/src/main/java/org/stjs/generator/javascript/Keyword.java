package org.stjs.generator.javascript;

import org.mozilla.javascript.Token;

public enum Keyword {
	THIS(Token.THIS), NULL(Token.NULL), TRUE(Token.TRUE), FALSE(Token.FALSE);

	private final int javaScript;

	private Keyword(int javaScript) {
		this.javaScript = javaScript;
	}

	public int getJavaScript() {
		return javaScript;
	}

}
