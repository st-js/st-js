package org.stjs.generator.handlers;

import static org.stjs.generator.GeneratedScriptTester.handlerTester;
import static org.stjs.generator.NodesFactory.bodyDeclarator;
import static org.stjs.generator.NodesFactory.newClassOrIntefarceDeclaration;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ClassOrInterfaceDeclarationHandlerTest {

	@Test
	public void shouldPrintNameWithMembers() throws Exception {
		handlerTester(ClassOrInterfaceDeclarationHandler.class, true).assertGenerateString(
				"functionName={arg1,arg2};",
				newClassOrIntefarceDeclaration().withName("functionName").addBodyDeclaration(bodyDeclarator("arg1"))
						.addBodyDeclaration(bodyDeclarator("arg2")).build());
	}

	@Test
	public void shouldHandleFunctionWiNoBody() throws Exception {
		handlerTester(ClassOrInterfaceDeclarationHandler.class, true).assertGenerateString("functionName={};",
				newClassOrIntefarceDeclaration().withName("functionName").build());
	}

}
