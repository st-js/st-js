package org.stjs.generator;

import static org.stjs.generator.GeneratedScriptTester.handlerTester;
import static org.stjs.generator.NodesFactory.newClassOrIntefarceDeclaration;

import org.junit.Test;
import org.stjs.generator.handlers.ClassOrInterfaceDeclarationHandler;

public class GeneratedSciptTesterTest {

	@Test(expected=AssertionError.class)
	public void shouldThrowAnAssertionError() throws Exception {
		handlerTester(ClassOrInterfaceDeclarationHandler.class, false)
		.assertGenerateString("XXX",
				newClassOrIntefarceDeclaration()
						.withName("functionName")
						.build());
	}
}
