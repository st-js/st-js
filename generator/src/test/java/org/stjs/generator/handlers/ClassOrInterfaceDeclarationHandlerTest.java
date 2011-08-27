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
import static org.stjs.generator.NodesFactory.bodyDeclarator;
import static org.stjs.generator.NodesFactory.methodDeclaration;
import static org.stjs.generator.NodesFactory.newClassOrIntefarceDeclaration;
import static org.stjs.generator.NodesFactory.parameter;
import static org.stjs.generator.scope.path.QualifiedPath.withClassName;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.VoidType;

import org.junit.Test;
import org.stjs.generator.scope.JavaTypeName;
import org.stjs.generator.scope.TypeScope;
import org.stjs.generator.scope.path.QualifiedPath;

public class ClassOrInterfaceDeclarationHandlerTest {

	@Test
	public void shouldPrintNameWithMembers() throws Exception {
		handlerTester(ClassOrInterfaceDeclarationHandler.class, true).assertGenerateString(
				"functionName=function(){}functionName.prototype.body1;functionName.prototype.body2;",
				newClassOrIntefarceDeclaration().withName("functionName").addBodyDeclaration(bodyDeclarator("body1;"))
						.addBodyDeclaration(bodyDeclarator("body2;")).build());
	}

	@Test
	public void shouldHandleFunctionWiNoBody() throws Exception {
		handlerTester(ClassOrInterfaceDeclarationHandler.class, true).assertGenerateString("functionName=function(){}",
				newClassOrIntefarceDeclaration().withName("functionName").build());
	}
	
	@Test
	public void shouldAddMethodCallForMainMethods() throws Exception {
		ReferenceType paramType = new ReferenceType(new ClassOrInterfaceType(
				"String"), 1);
		handlerTester(ClassOrInterfaceDeclarationHandler.class, true)
			.doNotPropagateInnerNodesVisits()
			.assertGenerateString(
					"main=function(){}main.MyClass.main();",
						newClassOrIntefarceDeclaration()
						.withName("main")
						.addBodyDeclaration(
							methodDeclaration("main",new VoidType(),parameter("args", paramType)))
						.withData(new TypeScope(null, null, new JavaTypeName(withClassName("MyClass")), null))
						.build())
			.assertVisitorCount(MethodDeclaration.class,1);
	}

}
