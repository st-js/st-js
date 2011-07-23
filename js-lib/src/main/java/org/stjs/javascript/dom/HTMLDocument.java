package org.stjs.javascript.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

abstract public class HTMLDocument {
	public String URL;
	public String documentURI;
	public HTMLCollection<HTMLAnchorElement> anchors;
	public HTMLCollection<HTMLAppletElement> applets;
	public HTMLElement body;
	public String cookie;
	public String domain;
	public HTMLCollection<HTMLFormElement> forms;
	public HTMLCollection<HTMLImageElement> images;
	public HTMLCollection<HTMLLinkElement> links;
	public String referrer;
	public String title;
	public HTMLElement documentElement;

	abstract public HTMLList<HTMLElement> getElementsByName(String arg0);

	abstract public HTMLElement getElementById(String id);

	abstract public HTMLList<HTMLElement> getElementsByTagName(String tagName);

	abstract public void writeln(String arg0);

	abstract public void write(String arg0);

	abstract public void close();

	abstract public void open();

	abstract public Element createElement(String tagName);

	abstract public Text createTextNode(String data);

	abstract public Attr createAttribute(String name);
}
