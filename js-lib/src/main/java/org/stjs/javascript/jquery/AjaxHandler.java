package org.stjs.javascript.jquery;

import org.stjs.javascript.XMLHttpRequest;

public interface AjaxHandler {
	public void handle(Event event, XMLHttpRequest request, AjaxParams params);
}
