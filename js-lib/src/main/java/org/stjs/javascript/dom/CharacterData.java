package org.stjs.javascript.dom;

abstract public class CharacterData extends Node {
	abstract public void appendData(String arg);

	abstract public void deleteData(int offset, int count);

	public String data;
	public int length;

	abstract public void insertData(int offset, String s);

	abstract public void replaceData(int offset, int count, String s);

	abstract public void substringData(int offset, int count);
}
