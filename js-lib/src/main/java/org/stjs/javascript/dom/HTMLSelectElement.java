package org.stjs.javascript.dom;

abstract public class HTMLSelectElement extends HTMLElement {
	public boolean disabled;
	public HTMLFormElement form;
	public int length;
	public boolean multiple;
	public String name;
	public HTMLCollection<HTMLOptionElement> options;
	public int selectedIndex;
	public int size;
	public int tabIndex;
	public String type;
	public String value;

	abstract public void blur();

	abstract public void add(HTMLElement arg0, HTMLElement arg1);

	abstract public void remove(int arg0);

	abstract public void focus();
}
