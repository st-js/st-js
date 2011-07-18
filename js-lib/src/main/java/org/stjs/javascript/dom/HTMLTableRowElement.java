package org.stjs.javascript.dom;

abstract public class HTMLTableRowElement extends HTMLElement {
	public String VAlign;
	public String align;
	public String bgColor;
	public HTMLCollection cells;
	public String ch;
	public String chOff;
	public int rowIndex;
	public int sectionRowIndex;

	abstract public void deleteCell(int arg0);

	abstract public HTMLElement insertCell(int arg0);
}
