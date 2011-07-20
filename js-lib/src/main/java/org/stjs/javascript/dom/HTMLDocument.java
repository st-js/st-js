package org.stjs.javascript.dom;

import org.w3c.dom.Document;

abstract public class HTMLDocument implements Document {
	public String URL;
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

	abstract public HTMLList<HTMLElement> getElementsByName(String arg0);

	abstract public void writeln(String arg0);

	abstract public void write(String arg0);

	abstract public void close();

	abstract public void open();
}
