package org.stjs.javascript;

import org.w3c.dom.css.CSSStyleRule;

//TODO :: see if it's best to use the W3C version
public abstract class StyleSheet {

	/**
	 * Gets the whole declaration. Works in IE
	 */
	public String cssText;

	/**
	 * Enables you to enable or disable the style Sheet
	 */
	public Boolean disabled;

	/**
	 * Source URL of the string
	 */
	public String href;

	/**
	 * all the rules of this stylesheet. Does not work in IE up to IE8
	 */
	public Array<CSSStyleRule> cssRules;

	/**
	 * Same as cssRules but for IE
	 */
	public Array<CSSStyleRule> rules;

	// media -> MediaList
	// ownerNode -> Element
	// ownerRule ->
	// parentStyleSheet ->
	// title
	// type -> String

	/**
	 * Works in IE and Webkit browsers
	 * @param selector
	 * @param rule
	 */
	public abstract void addRule(String selector, String rule);

	/**
	 * Works in IE and Webkit browsers
	 * @param selector
	 * @param rule
	 * @param where
	 */
	public abstract void addRule(String selector, String rule, Integer where);

	/**
	 * The complete rule goes into the first parameter Works in all Browsers but not IE below v9
	 * @param rule
	 * @param where
	 */
	public abstract void insertRule(String rule, Integer where);

	/**
	 * Works in all Browsers but not IE below v9
	 * @param rule_number
	 */
	public abstract void deleteRule(Integer rule_number);

	/**
	 * Works in IE and Webkit browsers
	 * @param rule_number
	 */
	public abstract void removeRule(Integer rule_number);
}
