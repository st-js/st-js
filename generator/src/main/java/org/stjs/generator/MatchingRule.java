package org.stjs.generator;

public class MatchingRule {
	private final String name;
	private final String rule;
	private final NodeHandlerWithPriority handler;

	public MatchingRule(String name, String rule, NodeHandlerWithPriority handler) {
		this.name = name;
		this.rule = rule;
		this.handler = handler;
	}

	public String getName() {
		return name;
	}

	public String getRule() {
		return rule;
	}

	public NodeHandlerWithPriority getHandler() {
		return handler;
	}

}
