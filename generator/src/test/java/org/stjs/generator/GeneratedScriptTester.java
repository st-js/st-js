package org.stjs.generator;

import static junit.framework.Assert.assertEquals;
import japa.parser.ast.Node;
import japa.parser.ast.visitor.VoidVisitor;
import java.io.File;
import org.stjs.generator.GenerationContextFactory.PartialGenerationContext;
import org.stjs.generator.handlers.RuleBasedVisitor;

public class GeneratedScriptTester {

	public static GeneratedScriptTester handlerTester(Class<? extends VoidVisitor<GenerationContext>> handlerClass,
			boolean trimAllWhiteSpaces) {
		return new GeneratedScriptTester(handlerClass, trimAllWhiteSpaces);
	}

	private final Class<? extends VoidVisitor<GenerationContext>> handlerClass;
	private final boolean trimAllWhiteSpaces;

	private GeneratedScriptTester(Class<? extends VoidVisitor<GenerationContext>> handlerClass,
			boolean trimAllWhiteSpaces) {
		this.handlerClass = handlerClass;
		this.trimAllWhiteSpaces = trimAllWhiteSpaces;
	}

	public void assertGenerateString(String expected, Node node) {
    assertGenerateString(expected, node, new GenerationContext(new File("test.java")));
  }
	
	public void assertGenerateString(String expected, Node node, GenerationContext context) {
		try {
			RuleBasedVisitor visitor = new RuleBasedVisitor();
			VoidVisitor<GenerationContext> handler = handlerClass.getConstructor(RuleBasedVisitor.class).newInstance(
					visitor);
			node.accept(handler, context);
			String got = visitor.getPrinter().toString();
			if (trimAllWhiteSpaces) {
				got = got.replaceAll("\\s", "");
			}
			assertEquals(expected, got);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

  public void assertGenerateString(String expected, Node node,
      PartialGenerationContext context) {
    assertGenerateString(expected, node, context.build());    
  }

}