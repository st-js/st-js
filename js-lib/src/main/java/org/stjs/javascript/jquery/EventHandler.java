package org.stjs.javascript.jquery;

import org.stjs.javascript.dom.HTMLElement;

public interface EventHandler {
	public boolean onEvent(Event ev, HTMLElement THIS);
}
