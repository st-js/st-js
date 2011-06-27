package org.stjs.generator.handlers;

import static org.stjs.generator.GeneratedScriptTester.handlerTester;
import static org.stjs.generator.NodesFactory.newFieldDeclaration;
import static org.stjs.generator.NodesFactory.variableDeclarator;

import org.junit.Test;


public class FieldDeclarationHandlerTest {

	@Test
	public void shouldPrintName() throws Exception {
		handlerTester(FieldDeclarationHandler.class, false).
		assertGenerateString("var x",
			newFieldDeclaration()
				.withVariable("x")
				.build());
	}
	
	@Test
	public void shouldPrintNameAndInitializer() throws Exception {
		handlerTester(FieldDeclarationHandler.class, false).
		assertGenerateString("var x = 3",
			newFieldDeclaration()
				.withVariable(variableDeclarator("x","3"))
				.build());
	}
	
	@Test
	public void shouldSupportMultipleDeclarations() throws Exception {
		handlerTester(FieldDeclarationHandler.class, false).
		assertGenerateString("var x = 3, y, z = 4",
			newFieldDeclaration()
				.withVariable(variableDeclarator("x","3"))
				.withVariable(variableDeclarator("y"))
				.withVariable(variableDeclarator("z","4"))
				.build());
	}


}
