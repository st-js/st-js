package org.stjs.javascript;

abstract public class Location {
	public String hash;
	public String host;
	public String hostname;
	public String href;
	public String pathname;
	public int port;
	public String protocal;
	public String search;

	abstract public void assign(String url);

	abstract public void reload();

	abstract public void replace(String url);
}
