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
package org.stjs.generator.scope;

import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.Option;
import org.stjs.generator.variable.Variable;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.javascript.annotation.JavascriptFunction;

/**
 * this class generate different checks made on the Java statements before they are converted to Javascript
 * 
 * @author acraciun
 * 
 */
public class Checks {

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
					if (!p.getId().getName().equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
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
				// allowed x = 1 and x = -1
				if (v.getInit() instanceof LiteralExpr) {
					return;
				}
				if (v.getInit() instanceof UnaryExpr) {
					if (((UnaryExpr) v.getInit()).getExpr() instanceof LiteralExpr) {
						return;
					}
				}
				throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(v),
						"Instance field inline initialization can only done with literal constants");
			}
		}
	}

	/**
	 * check a class declaration
	 * 
	 * @param n
	 * @param context
	 * @param scope
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
	 * other checks after the types were set
	 * 
	 * @param n
	 * @param context
	 */
	public static void postCheckClassDeclaration(ClassOrInterfaceDeclaration n, GenerationContext context) {
		if (n.getImplements() != null) {
			for (ClassOrInterfaceType impl : n.getImplements()) {
				TypeWrapper type = ASTNodeData.resolvedType(impl);
				if (type instanceof ClassWrapper
						&& ClassUtils.hasAnnotation((ClassWrapper) type, JavascriptFunction.class)) {
					throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
							"You cannot implement intefaces annotated with @JavascriptFunction. "
									+ "You can only have inline object creation with this type of interfaces");
				}
			}
		}
		String ns = ClassUtils.getNamespace(ASTNodeData.resolvedType(n));
		if (ns != null) {
			if (!GeneratorConstants.NAMESPACE_PATTERN.matcher(ns).matches()) {
				throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
						"The namespace must be in the form <identifier>[.<identifier>]..");
			}
		}
		if (n.getExtends() != null && !n.isInterface()) {
			TypeWrapper superType = ASTNodeData.resolvedType(n).getSuperClass();
			if (!(superType instanceof ClassWrapper)) {
				return;
			}
			ClassWrapper superClass = (ClassWrapper) superType;

			for (BodyDeclaration member : n.getMembers()) {
				if (member instanceof MethodDeclaration) {
					String name = ((MethodDeclaration) member).getName();
					MethodWrapper resolvedMethod = ASTNodeData.resolvedMethod(member);
					if (Modifier.isStatic(resolvedMethod.getModifiers())) {
						continue;
					}
					Option<MethodWrapper> overrideMethod = superClass.findMethod(name,
							resolvedMethod.getParameterTypes());
					if (overrideMethod.isDefined()) {
						if (Modifier.isPrivate(overrideMethod.getOrThrow().getModifiers())) {
							throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(member),
									"One of the parent types contains already a private method called [" + name
											+ "]. Javascript cannot distinguish methods/fields with the same name");
						}
						continue;
					}

					if (superClass.findField(name).isDefined() || !superClass.findMethods(name).isEmpty()) {
						throw new JavascriptGenerationException(
								context.getInputFile(),
								new SourcePosition(member),
								"One of the parent types contains already a method or a field called ["
										+ name
										+ "] with a different signature. Javascript cannot distinguish methods/fields with the same name");
					}
				} else if (member instanceof FieldDeclaration) {
					if (Modifier.isStatic(((FieldDeclaration) member).getModifiers())) {
						continue;
					}
					for (VariableDeclarator var : ((FieldDeclaration) member).getVariables()) {
						String name = var.getId().getName();
						if (superClass.findField(name).isDefined() || !superClass.findMethods(name).isEmpty()) {
							throw new JavascriptGenerationException(
									context.getInputFile(),
									new SourcePosition(member),
									"One of the parent types contains already a method or a field called ["
											+ name
											+ "] with a different signature. Javascript cannot distinguish methods/fields with the same name");
						}
					}
				}
			}
		}

	}

	/**
	 * check enum declaration
	 * 
	 * @param n
	 * @param context
	 */
	public static void checkEnumDeclaration(EnumDeclaration n, GenerationContext context) {
		if (n.getMembers() != null && n.getMembers().size() > 0) {
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"Enums with fields or methods are not supported");
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

	public static void checkSuperExpr(SuperExpr n, GenerationContext context) {
		if (n.getClassExpr() != null) {
			throw new JavascriptGenerationException(
					context.getInputFile(),
					new SourcePosition(n),
					"In Javascript you cannot call methods or fields from the outer type. "
							+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}
	}

	public static void checkScope(MethodCallExpr n, GenerationContext context) {
		if (n.getScope() == null) {
			if (!isAccessInCorrectScope(n)) {
				throw new JavascriptGenerationException(
						context.getInputFile(),
						new SourcePosition(n),
						"In Javascript you cannot call methods or fields from the outer type. "
								+ "You should define a variable var that=this outside your function definition and call the methods on this object");
			}
		}

	}

	public static void checkScope(NameExpr n, GenerationContext context) {
		if (!isAccessInCorrectScope(n)) {
			throw new JavascriptGenerationException(
					context.getInputFile(),
					new SourcePosition(n),
					"In Javascript you cannot call methods or fields from the outer type. "
							+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}

	}

	private static boolean isAccessInCorrectScope(MethodCallExpr n) {
		Scope scope = ASTNodeData.scope(n);
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
		if (Modifier.isStatic(method.getModifiers())) {
			return true;
		}
		TypeWrapper methodDeclaringClass = method.getOwnerType();
		ClassScope thisClassScope = scope.closest(ClassScope.class);
		return thisClassScope.getClazz().equals(methodDeclaringClass);
	}

	private static boolean isAccessInCorrectScope(NameExpr n) {
		Scope scope = ASTNodeData.scope(n);
		Variable var = ASTNodeData.resolvedVariable(n);
		if (!(var instanceof FieldWrapper)) {
			return true;
		}
		FieldWrapper field = (FieldWrapper) var;
		if (Modifier.isStatic(field.getModifiers())) {
			return true;
		}
		TypeWrapper declaringClass = field.getOwnerType();
		ClassScope thisClassScope = scope.closest(ClassScope.class);
		return thisClassScope.getClazz().equals(declaringClass);
	}

	public static void checkObjectCreationExpr(ObjectCreationExpr n, GenerationContext context) {
		TypeWrapper type = ASTNodeData.resolvedType(n.getType());
		if (!ClassUtils.isJavascriptFunction(type)) {
			return;
		}
		if (n.getAnonymousClassBody() != null && n.getAnonymousClassBody().size() > 1) {
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"Initialization block for a Javascript function must contain exactly one method");
		}

	}

	/**
	 * makes sure only literals can be used as keys (so the even arguments)
	 * 
	 * @param n
	 * @param context
	 */
	public static void checkMapConstructor(MethodCallExpr n, GenerationContext context) {
		if (n.getArgs() != null) {
			for (int i = 0; i < n.getArgs().size(); i += 2) {
				if (!(n.getArgs().get(i) instanceof LiteralExpr)) {
					throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
							"The key of a map built this way can only be a literal. Use map.$put(variable) if you want to use variables as keys");
				}

			}
		}
	}

	private static boolean isLoop(Node n) {
		return n instanceof ForeachStmt || n instanceof ForStmt || n instanceof WhileStmt || n instanceof DoStmt;
	}

	private static boolean isMethodOrClassDeclaration(Node n) {
		return n instanceof MethodDeclaration || n instanceof ConstructorDeclaration
				|| n instanceof ClassOrInterfaceDeclaration;
	}

	public static void checkVariableDeclarationExpr(VariableDeclarationExpr n, GenerationContext context) {
		if (!ModifierSet.isFinal(n.getModifiers())) {
			return;
		}
		for (Node p = n; p != null; p = ASTNodeData.parent(p)) {
			if (isLoop(p)) {
				throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
						"To prevent unexpected behaviour in Javascript, final variables must be declared at method level and not inside loops");
			}
			if (isMethodOrClassDeclaration(p)) {
				break;
			}
		}
	}

}
