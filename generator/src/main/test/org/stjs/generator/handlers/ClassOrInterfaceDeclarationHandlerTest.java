package org.stjs.generator.handlers;

import static org.stjs.generator.GeneratedScriptTester.handlerTester;
import static org.stjs.generator.NodesFactory.newClassOrIntefaceDeclaration;
import static org.stjs.generator.NodesFactory.stub;

import org.junit.Test;


public class ClassOrInterfaceDeclarationHandlerTest {

	@Test
	public void shouldPrintNameWithMembers() throws Exception {
		handlerTester(ClassOrInterfaceDeclarationHandler.class).
		assertGenerateString("functionName={arg1,arg2};",
				newClassOrIntefaceDeclaration()
							.withName("functionName")
							.addBodyDeclaration(stub("arg1"))
							.addBodyDeclaration(stub("arg2"))
							.build());
	}
	
	
}
