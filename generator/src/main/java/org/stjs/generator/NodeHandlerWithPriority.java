package org.stjs.generator;

import org.stjs.generator.handlers.DefaultHandler;

public class NodeHandlerWithPriority {
	private final DefaultHandler handler;
	private final int priority;

	public NodeHandlerWithPriority(DefaultHandler handler, int priority) {
		this.handler = handler;
		this.priority = priority;
	}

	public DefaultHandler getHandler() {
		return handler;
	}

	public int getPriority() {
		return priority;
	}

}
