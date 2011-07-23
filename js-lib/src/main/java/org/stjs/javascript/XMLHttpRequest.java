package org.stjs.javascript;

import org.stjs.javascript.dom.HTMLElement;

abstract public class XMLHttpRequest {
	public Runnable onreadystatechange;
	public int readyState;
	public String responseText;
	// is in fact DOM Element, but they were mixed
	public HTMLElement responseXML;
	public int status;
	public String statusText;

	abstract public void abort();

	abstract public String getAllResponseHeaders();

	abstract public String getResponseHeader(String name);

	abstract public void open(String method, String url);

	abstract public void open(String method, String url, boolean async, String uname, String pswd);

	abstract public void open(String method, String url, boolean async);

	abstract public void send();

	abstract public void send(String data);

	abstract public void setRequestHeader(String name, String value);
}
