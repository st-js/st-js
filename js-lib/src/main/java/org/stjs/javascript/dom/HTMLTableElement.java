package org.stjs.javascript.dom;
abstract public class HTMLTableElement extends HTMLElement{
public HTMLCollection TBodies;
public HTMLTableSectionElement TFoot;
public HTMLTableSectionElement THead;
public String align;
public String bgColor;
public String border;
public HTMLTableCaptionElement caption;
public String cellPadding;
public String cellSpacing;
public String frame;
public HTMLCollection rows;
public String rules;
public String summary;
public String width;
abstract public HTMLElement createCaption();
abstract public HTMLElement createTFoot();
abstract public HTMLElement createTHead();
abstract public void deleteCaption();
abstract public void deleteRow(int arg0);
abstract public void deleteTFoot();
abstract public void deleteTHead();
abstract public HTMLElement insertRow(int arg0);
}
