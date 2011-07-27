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

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;

public class ClassOrInterfaceDeclarationHandler extends DefaultHandler {

	public ClassOrInterfaceDeclarationHandler(RuleBasedVisitor ruleVisitor) {
		super(ruleVisitor);
	}

	private int getModifiers(BodyDeclaration member) {
		if (member instanceof FieldDeclaration) {
			return ((FieldDeclaration) member).getModifiers();
		}
		if (member instanceof MethodDeclaration) {
			return ((MethodDeclaration) member).getModifiers();
		}
		return 0;
	}

	private void printMembers(String className, List<BodyDeclaration> members, GenerationContext arg) {
		for (int i = 0; i < members.size(); ++i) {
			BodyDeclaration member = members.get(i);
			if (member instanceof ConstructorDeclaration) {
				continue;
			}
			getPrinter().printLn();
			getPrinter().printLn();
			int memberModifiers = getModifiers(member);
			getPrinter().print(className);
			if (!ModifierSet.isStatic(memberModifiers)) {
				getPrinter().print(".prototype");
			}
			getPrinter().print(".");
			member.accept(getRuleVisitor(), arg);
		}
	}

	private ConstructorDeclaration getConstructor(List<BodyDeclaration> members, GenerationContext arg) {
		ConstructorDeclaration constr = null;
		for (BodyDeclaration member : members) {
			if (member instanceof ConstructorDeclaration) {
				if (constr != null) {
					throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(member),
							"Only maximum one constructor is allowed");
				} else {
					constr = (ConstructorDeclaration) member;
				}
			}
		}
		return constr;
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, GenerationContext arg) {
		// TODO how to handle interfaces !?
		getPrinter().print(n.getName() + " = ");

		if (n.getMembers() != null) {
			ConstructorDeclaration constr = getConstructor(n.getMembers(), arg);
			if (constr != null) {
				constr.accept(getRuleVisitor(), arg);
			} else {
				getPrinter().printLn("function(){}");
			}

			if (n.getExtends() != null && n.getExtends().size() > 0) {
				getPrinter().printLn();
				getPrinter().printLn("stjs.extend(" + n.getName() + ", " + n.getExtends().get(0).getName() + ");");
			}
			printMembers(n.getName(), n.getMembers(), arg);
		}
	}

}
