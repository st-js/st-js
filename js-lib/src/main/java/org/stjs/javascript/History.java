package org.stjs.javascript;

abstract public class History {
	public int length;

	abstract public void back();

	abstract public void forward();

	abstract public void go(int positions);

	abstract public void go(String url);
}
