package org.stjs.javascript;

import org.stjs.javascript.dom.HTMLDocument;

abstract public class Window {
	public boolean closed;
	public String defaultStatus;
	public HTMLDocument document;
	public Window[] frames;
	public History history;
	public int innerHeight;
	public int innerWidth;
	public int length;
	public Location location;
	public String name;
	public Navigator navigator;
	public Window opener;
	public int outerHeight;
	public int outerWidth;
	public int pageXOffset;
	public int pageYOffset;
	public Window parent;
	public Screen screen;
	public int screenLeft;
	public int screenTop;
	public int screenX;
	public int screenY;
	public Window self;
	public String status;
	public Window top;

	abstract public void blur();

	abstract public void close();

	abstract public Window createPopup();

	abstract public void focus();

	abstract public void moveBy(int x, int y);

	abstract public void moveTo(int x, int y);

	abstract public Window open(String url);

	abstract public Window open(String url, String target);

	abstract public Window open(String url, String target, String specs, boolean replace);

	abstract public void print();

	abstract public void resizeBy(int w, int h);

	abstract public void resizeTo(int w, int h);

	abstract public void scrollBy(int x, int y);

	abstract public void scrollTo(int x, int y);
}
