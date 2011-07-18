package org.stjs.javascript.dom;

import org.w3c.dom.Node;

abstract public class HTMLCollection {
	public int length;

	abstract public Node namedItem(String arg0);

	abstract public Node item(int arg0);
}
