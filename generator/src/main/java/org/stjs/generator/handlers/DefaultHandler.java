package org.stjs.generator.handlers;

import japa.parser.ast.visitor.VoidVisitorAdapter;

import org.stjs.generator.handlers.DumpVisitor.SourcePrinter;


public abstract class DefaultHandler extends VoidVisitorAdapter<Object> {
	private final RuleBasedVisitor ruleVisitor;

	public DefaultHandler(RuleBasedVisitor ruleVisitor) {
		this.ruleVisitor = ruleVisitor;
	}

	public RuleBasedVisitor getRuleVisitor() {
		return ruleVisitor;
	}

	public SourcePrinter getPrinter() {
		return ruleVisitor.getPrinter();
	}

}
