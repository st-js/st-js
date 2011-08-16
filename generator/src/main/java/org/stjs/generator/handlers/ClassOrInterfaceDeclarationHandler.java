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

import static japa.parser.ast.body.ModifierSet.isStatic;
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
import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.JavaTypeName;
import org.stjs.generator.scope.TypeScope;

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
		if (member instanceof ClassOrInterfaceDeclaration) {
			return ((ClassOrInterfaceDeclaration) member).getModifiers();
		}
		throw new UnsupportedOperationException(
				"Expected field, method or class");
	}

	private void printMembers(ClassOrInterfaceDeclaration n,
			GenerationContext context) {
		List<BodyDeclaration> members = n.getMembers();
		for (BodyDeclaration member : members) {
			if (member instanceof ConstructorDeclaration) {
				continue;
			}
			printer.printLn();
			printer.printLn();
			int memberModifiers = getModifiers(member);
			if (ModifierSet.isAbstract(memberModifiers)
					|| ModifierSet.isNative(memberModifiers)) {
				continue;
			}
			TypeScope typeScope = (TypeScope) n.getData();
			JavaTypeName declaredClassName = typeScope.getDeclaredTypeName();
			if (isStatic(n.getModifiers())) {
				Option<String> fullyQualifiedString = declaredClassName
						.getFullName(false);
				if (fullyQualifiedString.isEmpty()) {
					throw new JavascriptGenerationException(
							context.getInputFile(), new SourcePosition(n),
							"definition of static members of anonymous classes is not supported");
				}
				printer.print(fullyQualifiedString.getOrThrow());
			} else {
				printer.print(n.getName());
			}
			if (!isStatic(memberModifiers)) {
				printer.print(".prototype");
			}
			printer.print(".");
			member.accept(getRuleVisitor(), context);
		}
	}

	private ConstructorDeclaration getConstructor(
			List<BodyDeclaration> members, GenerationContext arg) {
		ConstructorDeclaration constr = null;
		for (BodyDeclaration member : members) {
			if (member instanceof ConstructorDeclaration) {
				if (constr != null) {
					throw new JavascriptGenerationException(arg.getInputFile(),
							new SourcePosition(member),
							"Only maximum one constructor is allowed");
				} else {
					constr = (ConstructorDeclaration) member;
				}
			}
		}
		return constr;
	}

	@Override
	public void visit(final ClassOrInterfaceDeclaration n,
			final GenerationContext arg) {
		if (n.isInterface()) {
			return;
		}
		printer.print(n.getName() + " = ");
		if (n.getMembers() != null) {
			ConstructorDeclaration constr = getConstructor(n.getMembers(), arg);
			if (constr != null) {
				constr.accept(getRuleVisitor(), arg);
			} else {
				printer.printLn("function(){}");
			}

			if (n.getExtends() != null && n.getExtends().size() > 0) {
				printer.printLn();
				printer.printLn("stjs.extend(" + n.getName() + ", "
						+ n.getExtends().get(0).getName() + ");");
			}
			printMembers(n, arg);
		}
	}

}
