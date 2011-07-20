package org.stjs.javascript.dom;


abstract public class HTMLTableSectionElement extends HTMLElement {
	public String VAlign;
	public String align;
	public String ch;
	public String chOff;
	public HTMLCollection<HTMLTableRowElement> rows;

	abstract public void deleteRow(int arg0);

	abstract public HTMLElement insertRow(int arg0);
}
