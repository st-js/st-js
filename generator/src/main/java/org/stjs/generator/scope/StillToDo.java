package org.stjs.generator.scope;

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

import static org.stjs.generator.ast.ASTNodeData.resolvedVariable;
import japa.parser.ast.Node;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.lang.reflect.Modifier;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.variable.Variable;
import org.stjs.generator.writer.JavascriptKeywords;
import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Native;

/**
 * this class generate different checks made on the Java statements before they are converted to Javascript
 * @author acraciun
 */
public final class StillToDo {
	private StillToDo() {
		//
	}

	private static boolean isMoreGenericVarArg(MethodWrapper more, MethodWrapper less) {
		for (int p = 0; p < less.getParameterTypes().length; ++p) {
			if (!more.getVarargParamType().isAssignableFrom(less.getParameterTypes()[p])) {
				return false;
			}
		}
		return true;
	}

	private static boolean isMoreGenericNormalArgs(MethodWrapper more, MethodWrapper less) {
		for (int p = 0; p < less.getParameterTypes().length; ++p) {
			if (!more.getParameterTypes()[p].isAssignableFrom(less.getParameterTypes()[p])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * return true if the "more" method can be called with arguments that have the type of the "less" method. i.e. is
	 * more generic
	 * @param more
	 * @param less
	 * @return
	 */
	@SuppressWarnings("PMD.CompareObjectsWithEquals")
	private static boolean isMoreGeneric(MethodWrapper more, MethodWrapper less) {
		if (more == less) {
			return true;
		}
		// assumes only a var arg parameter is allowed @see checkVarArgs
		if (more.getParameterTypes().length == 1 && more.getVarargParamType() != null) {
			return isMoreGenericVarArg(more, less);
		}

		if (less.getParameterTypes().length > more.getParameterTypes().length) {
			return false;
		}

		return isMoreGenericNormalArgs(more, less);
	}

	private static void checkImplements(ClassOrInterfaceDeclaration n, GenerationContext context) {
		if (n.getImplements() != null) {
			for (ClassOrInterfaceType impl : n.getImplements()) {
				TypeWrapper type = ASTNodeData.resolvedType(impl);
				if (type instanceof ClassWrapper && ClassUtils.hasAnnotation((ClassWrapper) type, JavascriptFunction.class)) {
					throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
							"You cannot implement intefaces annotated with @JavascriptFunction. "
									+ "You can only have inline object creation with this type of interfaces");
				}
			}
		}
	}

	private static void checkOneMethodNonNative(BodyDeclaration member, GenerationContext context) {
		if (!(member instanceof MethodDeclaration)) {
			return;
		}
		MethodDeclaration methodDeclaration = (MethodDeclaration) member;
		if (ModifierSet.isNative(methodDeclaration.getModifiers())) {
			return;
		}
		String name = methodDeclaration.getName();

		MethodWrapper methodWithBody = ASTNodeData.resolvedMethod(methodDeclaration);
		ClassWrapper clazz = (ClassWrapper) methodWithBody.getOwnerType();
		List<MethodWrapper> overloaded = clazz.findMethods(name);
		if (overloaded.size() == 1) {
			return;
		}

		// check that the non-native method is the most generic one (that is it can be called with the parameters of
		// the other ones 1 some null)

		for (MethodWrapper m : overloaded) {

			if (!isMoreGeneric(methodWithBody, m)) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(member), "The method with the name ["
						+ name + "] that has a body is less generic than:" + m);
			}
		}
	}

	private static void checkOneNonNativeConstructor(BodyDeclaration member, GenerationContext context) {
		if (!(member instanceof ConstructorDeclaration)) {
			return;
		}
		ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) member;
		MethodWrapper constructorWrapper = ASTNodeData.resolvedMethod(constructorDeclaration);
		if (constructorWrapper.getAnnotationDirectly(Native.class) != null) {
			return;
		}

		ClassWrapper clazz = (ClassWrapper) constructorWrapper.getOwnerType();
		List<MethodWrapper> overloaded = clazz.findConstructors();
		if (overloaded.size() == 1) {
			return;
		}

		// check that the non-native method is the most generic one (that is it can be called with the parameters of
		// the other ones 1 some null)
		for (MethodWrapper m : overloaded) {
			if (!isMoreGeneric(constructorWrapper, m)) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(member),
						"The constructor that has a body is less generic than:" + m);
			}
		}
	}

	/**
	 * other checks after the types were set
	 * @param n
	 * @param context
	 */
	public static void postCheckClassDeclaration(ClassOrInterfaceDeclaration n, GenerationContext context) {
		checkImplements(n, context);
		checkNamespace(n, context);
		checkGlobalScope(n, context);

		// check native
		for (BodyDeclaration member : n.getMembers()) {
			checkOneNonNativeConstructor(member, context);
			checkOneMethodNonNative(member, context);
		}

	}

	/**
	 * global scope classes should only have static members
	 * @param n
	 * @param context
	 */
	private static void checkGlobalScope(ClassOrInterfaceDeclaration n, GenerationContext context) {
		if (ASTNodeData.resolvedType(n).hasAnnotation(GlobalScope.class)) {
			for (BodyDeclaration member : n.getMembers()) {
				if (!isStatic(member)) {
					throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
							"Only static constructions can be used in a @GlobalScope class");
				}
			}
		}
	}

	@SuppressWarnings("PMD.CyclomaticComplexity")
	private static boolean isStatic(BodyDeclaration n) {
		if (n instanceof AnnotationMemberDeclaration) {
			return ModifierSet.isStatic(((AnnotationMemberDeclaration) n).getModifiers());
		}
		if (n instanceof ConstructorDeclaration) {
			return false;
		}
		if (n instanceof EnumConstantDeclaration) {
			// TODO not sure this will be handled correctly by the global scope
			return true;
		}
		if (n instanceof FieldDeclaration) {
			return ModifierSet.isStatic(((FieldDeclaration) n).getModifiers());
		}
		if (n instanceof MethodDeclaration) {
			return ModifierSet.isStatic(((MethodDeclaration) n).getModifiers());
		}
		if (n instanceof TypeDeclaration) {
			return ModifierSet.isStatic(((TypeDeclaration) n).getModifiers());
		}
		if (n instanceof InitializerDeclaration) {
			return ((InitializerDeclaration) n).isStatic();
		}
		return false;
	}

	private static void checkNamespace(BodyDeclaration n, GenerationContext context) {
		String ns = ClassUtils.getNamespace(ASTNodeData.resolvedType(n));
		if (ns != null) {
			if (!GeneratorConstants.NAMESPACE_PATTERN.matcher(ns).matches()) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
						"The namespace must be in the form <identifier>[.<identifier>]..");
			}
			String[] identifiers = ns.split("\\.");
			for (String identifier : identifiers) {
				if (JavascriptKeywords.isReservedWord(identifier)) {
					throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n), "Identifier \"" + identifier
							+ "\" cannot be used as part of a namespace, " + "because it is a javascript keyword or a javascript reserved word");
				}
			}
		}
	}

	/**
	 * check enum declaration
	 * @param n
	 * @param context
	 */
	public static void checkEnumDeclaration(EnumDeclaration n, GenerationContext context) {
		if (n.getMembers() != null && n.getMembers().size() > 0) {
			throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
					"Enums with fields or methods are not supported");
		}

	}

	public static void postCheckEnumDeclaration(EnumDeclaration n, GenerationContext context) {
		checkNamespace(n, context);
	}

	/**
	 * check a name expression
	 * @param n
	 * @param context
	 */
	public static void checkNameExpr(NameExpr n, GenerationContext context) {
		JavascriptKeywords.checkIdentifier(context.getInputFile(), new SourcePosition(n), n.getName());
	}

	private static void throwCannotCallOuterType(Node n, GenerationContext context) {
		throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
				"In Javascript you cannot call methods or fields from the outer type. "
						+ "You should define a variable var that=this outside your function definition and call the methods on this object");

	}

	public static void checkThisExpr(ThisExpr n, GenerationContext context) {
		if (n.getClassExpr() != null) {
			throwCannotCallOuterType(n, context);
		}

	}

	public static void checkSuperExpr(SuperExpr n, GenerationContext context) {
		if (n.getClassExpr() != null) {
			throwCannotCallOuterType(n, context);
		}
	}

	public static void checkScope(MethodCallExpr n, GenerationContext context) {
		if (n.getScope() == null && !isAccessInCorrectScope(n)) {
			throwCannotCallOuterType(n, context);
		}
	}

	public static void checkScope(NameExpr n, GenerationContext context) {
		if (!isAccessInCorrectScope(n)) {
			throwCannotCallOuterType(n, context);
		}

	}

	private static boolean isAccessInCorrectScope(MethodCallExpr n) {
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
		if (Modifier.isStatic(method.getModifiers())) {
			return true;
		}
		TypeWrapper methodDeclaringClass = method.getOwnerType();
		Scope scope = ASTNodeData.scope(n);
		ClassScope thisClassScope = scope.closest(ClassScope.class);
		return thisClassScope.getClazz().equals(methodDeclaringClass);
	}

	private static boolean isAccessInCorrectScope(NameExpr n) {
		Variable var = ASTNodeData.resolvedVariable(n);
		if (!(var instanceof FieldWrapper)) {
			return true;
		}
		FieldWrapper field = (FieldWrapper) var;
		if (Modifier.isStatic(field.getModifiers())) {
			return true;
		}
		TypeWrapper declaringClass = field.getOwnerType();
		Scope scope = ASTNodeData.scope(n);
		ClassScope thisClassScope = scope.closest(ClassScope.class);
		return thisClassScope.getClazz().equals(declaringClass);
	}

	public static void checkObjectCreationExpr(ObjectCreationExpr n, GenerationContext context) {
		TypeWrapper type = ASTNodeData.resolvedType(n.getType());
		if (!ClassUtils.isJavascriptFunction(type)) {
			return;
		}
		if (n.getAnonymousClassBody() != null && n.getAnonymousClassBody().size() > 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
					"Initialization block for a Javascript function must contain exactly one method");
		}

	}

	/**
	 * makes sure only literals can be used as keys (so the even arguments)
	 * @param n
	 * @param context
	 */
	public static void checkMapConstructor(MethodCallExpr n, GenerationContext context) {
		if (n.getArgs() != null) {
			for (int i = 0; i < n.getArgs().size(); i += 2) {
				if (!(n.getArgs().get(i) instanceof LiteralExpr)) {
					throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
							"The key of a map built this way can only be a literal. "
									+ "Use map.$put(variable) if you want to use variables as keys");
				}

			}
		}
	}

	public static void checkGlobalVariable(NameExpr n, GenerationContext context, Scope currentScope) {
		Variable var = resolvedVariable(n);
		if (var instanceof FieldWrapper) {
			FieldWrapper field = (FieldWrapper) var;
			TypeWrapper scopeType = field.getOwnerType();
			checkGlobalVariable(n, scopeType, field.getName(), currentScope, context);
		}
	}

	public static void checkGlobalVariable(FieldAccessExpr n, GenerationContext context, Scope currentScope) {
		FieldWrapper field = (FieldWrapper) resolvedVariable(n);
		TypeWrapper scopeType = field.getOwnerType();
		checkGlobalVariable(n, scopeType, field.getName(), currentScope, context);
	}

	private static void checkGlobalVariable(Node n, TypeWrapper scopeType, String name, Scope currentScope, GenerationContext context) {
		if (scopeType.hasAnnotation(GlobalScope.class)) {
			// look if there is any variable with the same name in the method
			MethodScope methodScope = currentScope.closest(MethodScope.class);
			if (methodScope == null) {
				return;
			}
			Variable existent = resolveVariableInDescendantBlocks(methodScope, name);
			if (existent != null) {
				throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
						"A variable with the same name as your global variable is already defined in this method's scope. "
								+ "Please rename either the local variable/parameter or the global variable.");
			}
		}
	}

	/**
	 * look for the variable in all the blocks (without going in other types)
	 * @param methodScope
	 * @param name
	 * @return
	 */
	private static Variable resolveVariableInDescendantBlocks(Scope scope, String name) {
		VariableWithScope existent = scope.resolveVariable(name);
		if (existent != null && existent.getScope() == scope) {
			// take only this scope, not a parent class scope
			return existent.getVariable();
		}
		for (Scope child : scope.getChildren()) {
			if (child instanceof BasicScope) {
				Variable var = resolveVariableInDescendantBlocks(child, name);
				if (var != null) {
					return var;
				}
			}
		}
		return null;
	}
}
