package org.stjs.generator;

import static junit.framework.Assert.assertEquals;
import japa.parser.ast.Node;
import japa.parser.ast.visitor.VoidVisitor;

import org.stjs.generator.handlers.RuleBasedVisitor;

public class GeneratedScriptTester {
	
	public static GeneratedScriptTester handlerTester(Class<? extends VoidVisitor<?>> handlerClass,
			boolean trimAllWhiteSpaces) {
		return new GeneratedScriptTester(handlerClass, trimAllWhiteSpaces);
	}
	
	private final Class<? extends VoidVisitor<?>> handlerClass;
	private final boolean trimAllWhiteSpaces;
	
	private GeneratedScriptTester(Class<? extends VoidVisitor<?>> handlerClass, boolean trimAllWhiteSpaces) {
		this.handlerClass = handlerClass;
		this.trimAllWhiteSpaces = trimAllWhiteSpaces;
	}
	
	public void assertGenerateString(String expected, Node node) {
		try {
			RuleBasedVisitor visitor = new RuleBasedVisitor();
			VoidVisitor<?> handler = handlerClass.getConstructor(RuleBasedVisitor.class).newInstance(visitor);
			node.accept(handler, null);
			String got = visitor.getPrinter().toString();
			if (trimAllWhiteSpaces) {
				got = got.replaceAll("\\s", "");
			}
			assertEquals(expected, got);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}