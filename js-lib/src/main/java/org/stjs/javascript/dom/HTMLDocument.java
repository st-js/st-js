package org.stjs.javascript.dom;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

abstract public class HTMLDocument implements Document {
	public String URL;
	public HTMLCollection anchors;
	public HTMLCollection applets;
	public HTMLElement body;
	public String cookie;
	public String domain;
	public HTMLCollection forms;
	public HTMLCollection images;
	public HTMLCollection links;
	public String referrer;
	public String title;

	abstract public NodeList getElementsByName(String arg0);

	abstract public void writeln(String arg0);

	abstract public void write(String arg0);

	abstract public void close();

	abstract public void open();
}
