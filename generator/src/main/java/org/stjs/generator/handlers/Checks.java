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
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ThisExpr;

import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.JavascriptKeywords;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.utils.ClassUtils;

/**
 * this class generate different checks made on the Java statements before they are converted to Javascript
 * 
 * @author acraciun
 * 
 */
public class Checks {

	private static final String VARARGS_ALLOWED_NAME = "arguments";

	/**
	 * check a method declaration
	 * 
	 * @param n
	 * @param arg
	 */
	public static void checkMethodDeclaration(MethodDeclaration n, GenerationContext arg) {
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				if (p.isVarArgs()) {
					if (!p.getId().getName().equals(VARARGS_ALLOWED_NAME)) {
						throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
								"You can only have a vararg parameter that has to be called 'arguments'");

					}
					if (n.getParameters().size() != 1) {
						throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
								"You can only have a vararg parameter that has to be called 'arguments'");

					}
				}
			}
		}

	}

	/**
	 * check a field declaration
	 * 
	 * @param n
	 * @param arg
	 */
	public static void checkFieldDeclaration(FieldDeclaration n, GenerationContext arg) {
		for (VariableDeclarator v : n.getVariables()) {
			JavascriptKeywords.checkIdentifier(arg.getInputFile(), new SourcePosition(v), v.getId().getName());
			if (!ModifierSet.isStatic(n.getModifiers()) && v.getInit() != null) {
				if (!ClassUtils.isBasicType(n.getType())) {
					throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(v),
							"Instance field inline initialization is allowed only for string and number field types");
				}
				if (!(v.getInit() instanceof LiteralExpr)) {
					throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(v),
							"Instance field inline initialization can only done with literal constants");
				}
			}
		}
	}

	/**
	 * check a class declaration
	 * 
	 * @param n
	 * @param context
	 */
	public static void checkClassDeclaration(ClassOrInterfaceDeclaration n, GenerationContext context) {
		if (n.getMembers() == null) {
			return;
		}
		ConstructorDeclaration constr = null;
		Set<String> names = new HashSet<String>();
		for (BodyDeclaration member : n.getMembers()) {
			if (member instanceof ConstructorDeclaration) {
				if (constr != null) {
					throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(member),
							"Only maximum one constructor is allowed");
				} else {
					constr = (ConstructorDeclaration) member;
				}
			} else if (member instanceof MethodDeclaration) {
				String name = ((MethodDeclaration) member).getName();
				if (!names.add(name)) {
					throw new JavascriptGenerationException(
							context.getInputFile(),
							new SourcePosition(member),
							"The type contains already a method or a field called ["
									+ name
									+ "] with a different signature. Javascript cannot distinguish methods/fields with the same name");
				}
			} else if (member instanceof FieldDeclaration) {
				for (VariableDeclarator var : ((FieldDeclaration) member).getVariables()) {
					String name = var.getId().getName();
					if (!names.add(name)) {
						throw new JavascriptGenerationException(
								context.getInputFile(),
								new SourcePosition(member),
								"The type contains already a method or a field called ["
										+ name
										+ "] with a different signature. Javascript cannot distinguish methods/fields with the same name");
					}
				}
			}
		}
	}

	/**
	 * check a name expression
	 * 
	 * @param n
	 * @param context
	 */
	public static void checkNameExpr(NameExpr n, GenerationContext context) {
		JavascriptKeywords.checkIdentifier(context.getInputFile(), new SourcePosition(n), n.getName());
	}

	public static void checkVariableDeclaratorId(VariableDeclaratorId n, GenerationContext context) {
		JavascriptKeywords.checkIdentifier(context.getInputFile(), new SourcePosition(n), n.getName());
	}

	public static void checkThisExpr(ThisExpr n, GenerationContext context) {
		if (n.getClassExpr() != null) {
			throw new JavascriptGenerationException(
					context.getInputFile(),
					new SourcePosition(n),
					"In Javascript you cannot call methods or fields from the outer type. "
							+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}

	}

}
