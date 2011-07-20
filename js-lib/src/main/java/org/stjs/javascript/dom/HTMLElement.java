package org.stjs.javascript.dom;

abstract public class HTMLElement extends Node {
	public String className;
	public String dir;
	public String id;
	public String lang;
	public String title;
	public String tagName;

	abstract public String getAttribute(String name);

	abstract public Attr getAttributeNode(String name);

	// abstract public getAttributeNodeNS(String, String)
	// abstract public getAttributeNS(String, String)
	abstract public HTMLList<HTMLElement> getElementsByTagName(String tag);

	// abstract public getElementsByTagNameNS(String, String)
	// abstract public getSchemaTypeInfo()

	abstract public boolean hasAttribute(String name);

	// abstract public hasAttributeNS(String, String)
	abstract public void removeAttribute(String name);

	abstract public void removeAttributeNode(Attr att);

	// abstract public removeAttributeNS(String, String)
	abstract public void setAttribute(String name, String value);

	abstract public void setAttributeNode(Attr name);

	// abstract public setAttributeNodeNS(Attr)
	// abstract public setAttributeNS(String, String, String)
	abstract public void setIdAttribute(String name, boolean id);

	abstract public void setIdAttributeNode(Attr attr, boolean id);

	// abstract public setIdAttributeNS(String, String, boolean)

}
