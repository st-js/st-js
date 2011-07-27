/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
