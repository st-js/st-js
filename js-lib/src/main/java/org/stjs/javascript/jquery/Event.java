package org.stjs.javascript.jquery;

import org.stjs.javascript.dom.HTMLElement;

/**
 * jquery event
 */
abstract public class Event {
	public HTMLElement target;
	public HTMLElement relatedTarget;
	public HTMLElement currentTarget;
	public int pageX;
	public int pageY;
	public int which;
	public String metaKey;
	public Object data;
	public String namespace;
	public Object result;
	public long timeStamp;
	public String type;

	abstract public boolean isDefaultPrevented();

	abstract public boolean isImmediatePropagationStopped();

	abstract public boolean isPropagationStopped();

	abstract public void preventDefault();

	abstract public void stopImmediatePropagation();

	abstract public void stopPropagation();
}
