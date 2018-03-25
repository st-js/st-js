package org.stjs.generator.javascript;

import org.mozilla.javascript.Token;

/**
 * <p>Keyword class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public enum Keyword {
	THIS(Token.THIS), NULL(Token.NULL), TRUE(Token.TRUE), FALSE(Token.FALSE);

	private final int javaScript;

	private Keyword(int javaScript) {
		this.javaScript = javaScript;
	}

	/**
	 * <p>Getter for the field <code>javaScript</code>.</p>
	 *
	 * @return a int.
	 */
	public int getJavaScript() {
		return javaScript;
	}

}
