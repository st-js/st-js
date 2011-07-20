package org.stjs.javascript.jquery.plugins;

import org.stjs.javascript.dom.HTMLElement;
import org.stjs.javascript.jquery.Event;

public interface UIEventHandler<UI> {
	public boolean onEvent(Event ev, UI ui, HTMLElement THIS);
}
