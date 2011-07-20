package org.stjs.javascript.dom;

abstract public class Attr extends Node {
	public String name;
	public HTMLElement ownerElement;
	// getSchemaTypeInfo()
	public boolean specified;
	public String value;
	public boolean isId;
}
