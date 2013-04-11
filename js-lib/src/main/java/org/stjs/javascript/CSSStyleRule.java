package org.stjs.javascript;

//TODO :: see if it's best to use the W3C version
public class CSSStyleRule {
	/**
	 * Gets/sets the textual representation of the selector for this rule, e.g. "h1,h2".
	 */
	public String selectorText;

	/**
	 * Returns the CSSStyleDeclaration object for the rule. Read only.
	 */
	public CSSStyleDeclaration style;
}
