package org.stjs.javascript.jquery;

import org.stjs.javascript.XMLHttpRequest;

public abstract class JQueryXHR extends XMLHttpRequest {
	String responseText;
	String responseXML;

	@Override
	abstract public String getResponseHeader(String header);

	abstract public void overrideMimeType(String type);
}
