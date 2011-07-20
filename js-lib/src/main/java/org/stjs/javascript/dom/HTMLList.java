package org.stjs.javascript.dom;

abstract public class HTMLList<T extends Node> {
	public int length;

	abstract public T item(int index);

	abstract public T $get(int index);
}
