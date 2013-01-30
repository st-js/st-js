package org.stjs.javascript;

//TODO :: see if it's best to use the W3C version
public abstract class CSSStyleDeclaration {
	/**
	 * Textual representation of the declaration block. Setting this attribute changes the style.
	 */
	public String cssText;

	/**
	 * The number of properties. See the item method below.
	 */
	public Integer length;

	/**
	 * The containing cssRule.
	 */
	public CSSStyleRule parentRule;

	/**
	 * @param propertyName
	 * @return the optional priority, "important".
	 */
	public abstract String getPropertyPriority(String propertyName);

	/**
	 * @param propertyName
	 * @return the property value
	 */
	public abstract String getPropertyValue(String propertyName);

	/**
	 * @param item
	 * @return the property name
	 */
	public abstract String index(Integer item);

	/**
	 * @param propertyName
	 * @return the value deleted
	 */
	public abstract String removeProperty(String propertyName);

	/**
	 * @param propertyName
	 * @param value
	 * @param priority (important or null)
	 */
	public abstract void setProperty(String propertyName, String value, String priority);
}
