package org.stjs.javascript.jquery;

/**
 * jquery event
 */
abstract public class Event {
	public DOMElement target;
	public DOMElement relatedTarget;
	public DOMElement currentTarget;
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
