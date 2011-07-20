package org.stjs.javascript.dom;

abstract public class HTMLCollection<T extends Node> {
	public int length;

	abstract public T namedItem(String name);

	abstract public T $get(String name);

	abstract public T item(int index);

	abstract public T $get(int index);
}
