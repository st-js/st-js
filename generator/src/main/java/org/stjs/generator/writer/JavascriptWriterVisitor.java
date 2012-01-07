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
package org.stjs.generator.writer;

import static japa.parser.ast.body.ModifierSet.isStatic;
import static org.stjs.generator.ast.ASTNodeData.checkParent;
import static org.stjs.generator.ast.ASTNodeData.parent;
import static org.stjs.generator.ast.ASTNodeData.resolvedMethod;
import static org.stjs.generator.ast.ASTNodeData.resolvedType;
import static org.stjs.generator.ast.ASTNodeData.resolvedVariable;
import static org.stjs.generator.ast.ASTNodeData.scope;
import japa.parser.ast.BlockComment;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import japa.parser.ast.visitor.VoidVisitor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.scope.ClassScope;
import org.stjs.generator.scope.Scope;
import org.stjs.generator.scope.ScopeBuilder;
import org.stjs.generator.type.ClassWrapper;
import org.stjs.generator.type.FieldWrapper;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.type.ParameterizedTypeWrapper;
import org.stjs.generator.type.TypeWrapper;
import org.stjs.generator.type.TypeWrappers;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.NodeUtils;
import org.stjs.generator.utils.Option;
import org.stjs.generator.utils.PreConditions;
import org.stjs.generator.variable.Variable;
import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.GlobalScope;

/**
 * This class visits the AST corresponding to a Java file and generates the corresponding Javascript code. It presumes
 * the {@link ScopeBuilder} previously visited the tree and set the resolved name of certain nodes.
 * 
 */
public class JavascriptWriterVisitor implements VoidVisitor<GenerationContext> {

	private final SpecialMethodHandlers specialMethodHandlers;

	JavascriptWriter printer = new JavascriptWriter();

	private List<Comment> comments;

	private int currentComment = 0;

	public JavascriptWriterVisitor() {
		specialMethodHandlers = new SpecialMethodHandlers();
	}

	public String getGeneratedSource() {
		return printer.getSource();
	}

	@Override
	public void visit(CompilationUnit n, GenerationContext context) {
		comments = n.getComments();
		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
				i.next().accept(this, context);
				printer.printLn();
				if (i.hasNext()) {
					printer.printLn();
				}
			}
		}
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext context) {
		printer.print(stJsName(resolvedType(n)));
	}

	private String stJsName(TypeWrapper typeWrapper) {
		// We may want to use a more complex naming scheme, to avoid conflicts across packages

		if (typeWrapper instanceof ClassWrapper) {
			return ((ClassWrapper) typeWrapper).getExternalName();
		}
		return typeWrapper.getType().toString();
	}

	@Override
	public void visit(ReferenceType n, GenerationContext context) {
		// skip
	}

	@Override
	public void visit(ImportDeclaration n, GenerationContext context) {
		// skip
	}

	@Override
	public void visit(PackageDeclaration n, GenerationContext context) {
		// skip
	}

	@Override
	public void visit(MarkerAnnotationExpr n, GenerationContext context) {
		// skip
	}

	@Override
	public void visit(SynchronizedStmt n, GenerationContext context) {
		throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
				"synchronized blocks are not supported by Javascript");
	}

	@Override
	public void visit(CastExpr n, GenerationContext context) {
		// skip to cast type - continue with the expression
		if (n.getExpr() != null) {
			n.getExpr().accept(this, context);
		}
	}

	@Override
	public void visit(IntegerLiteralExpr n, GenerationContext context) {
		printer.printNumberLiteral(n.getValue());
	}

	@Override
	public void visit(LongLiteralExpr n, GenerationContext context) {
		printer.printNumberLiteral(n.getValue());
	}

	@Override
	public void visit(StringLiteralExpr n, GenerationContext context) {
		printer.printStringLiteral(n.getValue());
	}

	@Override
	public void visit(CharLiteralExpr n, GenerationContext context) {
		printer.printCharLiteral(n.getValue());
	}

	@Override
	public void visit(DoubleLiteralExpr n, GenerationContext context) {
		printer.printNumberLiteral(n.getValue());
	}

	@Override
	public void visit(BooleanLiteralExpr n, GenerationContext context) {
		printer.printLiteral(Boolean.toString(n.getValue()));
	}

	public void print(StringLiteralExpr n) {
		// java has some more syntax to declare integers :
		// 0x0, 0b0, (java7) 1_000_000
		// TODO : convert it to plain numbers for javascript
		printer.printLiteral(n.getValue());
	}

	@Override
	public void visit(EnumDeclaration n, GenerationContext context) {
		printComments(n, context);
		// printer.print(n.getName());
		Scope scope = scope(n);
		printer.print(stJsName(scope.resolveType(n.getName()).getType()));
		// TODO implements not considered
		printer.print(" = ");
		printer.printLn(" stjs.enumeration(");
		printer.indent();
		if (n.getEntries() != null) {
			for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
				EnumConstantDeclaration e = i.next();
				printer.printStringLiteral(e.getName());
				if (i.hasNext()) {
					printer.printLn(", ");
				}
			}
		}
		// TODO members not considered
		printer.printLn("");
		printer.unindent();
		printer.print(");");
	}

	@Override
	public void visit(ForeachStmt n, GenerationContext context) {
		printer.print("for (");
		n.getVariable().accept(this, context);
		printer.print(" in ");
		n.getIterable().accept(this, context);
		printer.print(") ");
		boolean hasBlockBody = n.getBody() instanceof BlockStmt;

		// add braces when we have one line statement
		if (!hasBlockBody) {
			printer.printLn("{");
			printer.indent();
			generateArrayHasOwnProperty(n, context);
		}
		n.getBody().accept(this, context);

		if (!hasBlockBody) {
			printer.printLn();
			printer.unindent();
			printer.print("}");
		}
	}

	@Override
	public void visit(IfStmt n, GenerationContext context) {
		printer.print("if (");
		n.getCondition().accept(this, context);
		printer.print(") ");
		n.getThenStmt().accept(this, context);
		if (n.getElseStmt() != null) {
			printer.print(" else ");
			n.getElseStmt().accept(this, context);
		}
	}

	@Override
	public void visit(WhileStmt n, GenerationContext context) {
		printer.print("while (");
		n.getCondition().accept(this, context);
		printer.print(") ");
		n.getBody().accept(this, context);
	}

	@Override
	public void visit(ContinueStmt n, GenerationContext context) {
		printer.print("continue");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		printer.print(";");
	}

	@Override
	public void visit(DoStmt n, GenerationContext context) {
		printer.print("do ");
		n.getBody().accept(this, context);
		printer.print(" while (");
		n.getCondition().accept(this, context);
		printer.print(");");
	}

	@Override
	public void visit(ForStmt n, GenerationContext context) {
		printer.print("for (");
		if (n.getInit() != null) {
			for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, context);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print("; ");
		if (n.getCompare() != null) {
			n.getCompare().accept(this, context);
		}
		printer.print("; ");
		if (n.getUpdate() != null) {
			for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, context);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(") ");
		n.getBody().accept(this, context);
	}

	@Override
	public void visit(VariableDeclaratorId n, GenerationContext context) {
		if ((parent(n) instanceof Parameter) && n.getName().equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			// add an "_" for the arguments parameter to no override to arguments one.
			printer.print("_");
		}
		printer.print(n.getName());
	}

	@Override
	public void visit(VariableDeclarator n, GenerationContext context) {
		throw new IllegalStateException("Unexpected visit in a VariableDeclarator node:" + n);
	}

	private void printVariableDeclarator(VariableDeclarator n, GenerationContext context, boolean forceInitNull) {
		n.getId().accept(this, context);
		if (n.getInit() != null) {
			printer.print(" = ");
			n.getInit().accept(this, context);
		} else if (forceInitNull) {
			printer.print(" = null");
		}

	}

	@Override
	public void visit(VariableDeclarationExpr n, GenerationContext context) {
		// skip type
		printer.print("var ");

		for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
			VariableDeclarator v = i.next();
			printVariableDeclarator(v, context, false);
			if (i.hasNext()) {
				printer.print(", ");
			}
		}
	}

	@Override
	public void visit(FieldDeclaration n, GenerationContext context) {
		TypeWrapper type = resolvedType(parent(n));
		boolean skipType = isGlobal(type) && isStatic(n.getModifiers());
		String typeName = stJsName(type);
		// skip type
		for (VariableDeclarator v : n.getVariables()) {
			if (!skipType) {
				printer.print(typeName);
			}
			if (!isStatic(n.getModifiers())) {
				printer.print(".prototype");
			}
			if (!skipType) {
				printer.print(".");
			}

			printVariableDeclarator(v, context, true);
			printer.print(";");
		}
	}

	private void printJavadoc(JavadocComment javadoc, GenerationContext context) {
		if (javadoc != null) {
			javadoc.accept(this, context);
		}
	}

	private void printComments(Node n, GenerationContext context) {
		if (comments == null) {
			return;
		}
		// the problem is that the comments are all attached to the root node
		// so this method will display all the comments before the given node.
		while (currentComment < comments.size()) {
			if (comments.get(currentComment).getBeginLine() < n.getBeginLine()) {
				comments.get(currentComment).accept(this, context);
			} else {
				break;
			}
			currentComment++;
		}
	}

	private void printMethod(String name, List<Parameter> parameters, int modifiers, BlockStmt body,
			GenerationContext context, TypeWrapper type, boolean anonymous) {

		if (ModifierSet.isAbstract(modifiers) || ModifierSet.isNative(modifiers)) {
			return;
		}

		// no type appears for global scopes
		boolean skipType = isGlobal(type) && isStatic(modifiers);

		if (anonymous) {
			printer.print("function");
		} else {
			if (!skipType) {
				printer.print(stJsName(type));
			}
			if (!isStatic(modifiers)) {
				printer.print(".prototype");
			}
			if (!skipType) {
				printer.print(".");
			}
			printer.print(name);
			printer.print(" = function");
		}

		printer.print("(");
		if (parameters != null) {
			boolean first = true;
			for (Parameter p : parameters) {
				// don't display the special THIS parameter
				if (GeneratorConstants.SPECIAL_THIS.equals(p.getId().getName())) {
					continue;
				}
				if (!first) {
					printer.print(", ");
				}
				p.accept(this, context);
				first = false;
			}
		}
		printer.print(")");
		// skip throws
		if (body == null) {
			printer.print("{}");
		} else {
			printer.print(" ");
			body.accept(this, context);
		}
		if (!anonymous) {
			printer.print(";");
		}
	}

	private MethodDeclaration getMethodDeclaration(ObjectCreationExpr n) {
		MethodDeclaration singleMethod = null;
		for (BodyDeclaration d : n.getAnonymousClassBody()) {
			if (d instanceof MethodDeclaration) {
				if (singleMethod != null) {
					// there are more methods -> back to standard declaration
					return null;
				}
				singleMethod = (MethodDeclaration) d;
			} else if (d instanceof FieldDeclaration) {
				// back to standard declaration
				return null;
			}
		}
		return singleMethod;
	}

	public void printArguments(List<Expression> expressions, GenerationContext context) {
		printArguments(Collections.<String> emptyList(), expressions, Collections.<String> emptyList(), context);
	}

	public void printArguments(Collection<String> beforeParams, Collection<Expression> expressions,
			Collection<String> afterParams, GenerationContext context) {
		printer.print("(");
		boolean first = true;
		for (String param : beforeParams) {
			if (!first) {
				printer.print(", ");
			}
			printer.print(param);
			first = false;
		}
		if (expressions != null) {
			for (Expression e : expressions) {
				if (!first) {
					printer.print(", ");
				}
				e.accept(this, context);
				first = false;
			}
		}
		for (String param : afterParams) {
			if (!first) {
				printer.print(", ");
			}
			printer.print(param);
			first = false;
		}
		printer.print(")");
	}

	private InitializerDeclaration getInitializerDeclaration(ObjectCreationExpr n) {
		if (n.getAnonymousClassBody() == null) {
			return null;
		}
		for (BodyDeclaration d : n.getAnonymousClassBody()) {
			if (d instanceof InitializerDeclaration) {
				return (InitializerDeclaration) d;
			}
		}
		return null;
	}

	private ClassOrInterfaceDeclaration buildClassDeclaration(String className, ClassOrInterfaceType extendsFrom,
			List<BodyDeclaration> members, List<Expression> constructorArguments) {
		ClassOrInterfaceDeclaration decl = new ClassOrInterfaceDeclaration();
		decl.setName(className);
		decl.setExtends(Collections.singletonList(extendsFrom));
		decl.setMembers(members);
		// TODO add constructor if needed to call the super with the constructorscopeWalkers
		return decl;
	}

	@Override
	public void visit(ObjectCreationExpr n, GenerationContext context) {
		InitializerDeclaration block = getInitializerDeclaration(n);
		if (block != null) {
			// special construction for object initialization new Object(){{x = 1; y = 2; }};
			block.getBlock().accept(this, context);
			return;
		}

		TypeWrapper clazz = resolvedType(n.getType());

		if ((n.getAnonymousClassBody() != null) && (n.getAnonymousClassBody().size() >= 1)) {
			// special construction for inline function definition
			if (ClassUtils.isJavascriptFunction(clazz)) {
				MethodDeclaration method = getMethodDeclaration(n);
				PreConditions.checkStateNode(n, method != null, "A single method was expected for an inline function");
				if (method != null) {
					printMethod(method.getName(), method.getParameters(), method.getModifiers(), method.getBody(),
							context, resolvedType(n), true);
				}
				return;
			}
			// special construction to handle the inline body
			// build a special type called _InlineType to handle this

			printer.printLn("(function(){");
			ClassOrInterfaceDeclaration inlineFakeClass = buildClassDeclaration(GeneratorConstants.SPECIAL_INLINE_TYPE,
					n.getType(), n.getAnonymousClassBody(), n.getArgs());
			inlineFakeClass.setData(n.getData());
			inlineFakeClass.accept(this, context);

			printer.printLn("");
			printer.print("return new ").print(GeneratorConstants.SPECIAL_INLINE_TYPE);
			printArguments(n.getArgs(), context);
			printer.printLn(";");
			printer.print("})()");

			return;

		}

		if ((clazz != null) && (clazz instanceof ClassWrapper) && ClassUtils.isSyntheticType(clazz)) {
			// this is a call to an mock type
			printer.print("{}");
			return;
		}
		printer.print("new ");
		n.getType().accept(this, context);
		printArguments(n.getArgs(), context);
	}

	@Override
	public void visit(Parameter n, GenerationContext context) {
		// skip type
		n.getId().accept(this, context);
	}

	@Override
	public void visit(MethodDeclaration n, GenerationContext context) {
		printComments(n, context);
		printMethod(n.getName(), n.getParameters(), n.getModifiers(), n.getBody(), context, resolvedType(parent(n)),
				false);
	}

	private void addCallToSuper(ClassScope classScope, GenerationContext context, Collection<Expression> args) {
		PreConditions.checkNotNull(classScope);

		Option<ClassWrapper> superClass = classScope.getClazz().getSuperclass();

		if (superClass.isDefined()) {
			if (ClassUtils.isSyntheticType(superClass.getOrThrow())) {
				// do not add call to super class is it's a synthetic
				return;
			}

			if (superClass.getOrThrow().getClazz().equals(Object.class)) {
				// avoid useless call to super() when the super class is Object
				return;
			}
			printer.print(stJsName(superClass.getOrThrow())).print(".call");
			printArguments(Collections.singleton("this"), args, Collections.<String> emptyList(), context);
			printer.print(";");

		}
	}

	@Override
	public void visit(ConstructorDeclaration n, GenerationContext context) {
		printComments(n, context);
		Option<ClassWrapper> superClass = scope(n).closest(ClassScope.class).getClazz().getSuperclass();
		if (superClass.isDefined() && !ClassUtils.isSyntheticType(superClass.getOrThrow())) {
			if ((n.getBlock().getStmts() != null) && (n.getBlock().getStmts().size() > 0)) {
				Statement firstStatement = n.getBlock().getStmts().get(0);
				if (!(firstStatement instanceof ExplicitConstructorInvocationStmt)) {
					// generate possibly missing super() call
					Statement callSuper = new ExplicitConstructorInvocationStmt();
					callSuper.setData(new ASTNodeData());
					parent(callSuper, n.getBlock());
					scope(callSuper, scope(n.getBlock()));
					n.getBlock().getStmts().add(0, callSuper);
					// addCallToSuper(scope(n).closest(ClassScope.class), context, Collections.<Expression>
					// emptyList());
				}
			}
		}
		printMethod(n.getName(), n.getParameters(), n.getModifiers(), n.getBlock(), context, resolvedType(parent(n)),
				true);
	}

	@Override
	public void visit(TypeParameter n, GenerationContext context) {
		// skip

	}

	@Override
	public void visit(LineComment n, GenerationContext context) {
		printer.print("//");
		if (n.getContent().endsWith("\n")) {
			// remove trailing enter and printLn
			// to keep indentation
			printer.printLn(n.getContent().substring(0, n.getContent().length() - 1));
		}

	}

	@Override
	public void visit(BlockComment n, GenerationContext context) {
		printer.print("/*");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

	private List<TypeWrapper> getImplementsOrExtends(ClassOrInterfaceDeclaration n) {
		List<TypeWrapper> types = new ArrayList<TypeWrapper>();

		if (n.getExtends() != null) {
			for (ClassOrInterfaceType ext : n.getExtends()) {
				TypeWrapper type = resolvedType(ext);
				if (!ClassUtils.isSyntheticType(type)) {
					types.add(type);
				}

			}
		}
		if (n.getImplements() != null) {
			for (ClassOrInterfaceType impl : n.getImplements()) {
				TypeWrapper type = resolvedType(impl);
				if (!ClassUtils.isSyntheticType(type)) {
					types.add(type);
				}
			}
		}
		return types;
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, GenerationContext context) {
		printComments(n, context);

		ClassScope scope = (ClassScope) scope(n);
		if (resolvedType(n) == null) {
			// for anonymous object creation the type is set already
			resolvedType(n, scope.resolveType(n.getName()).getType());
		}

		String className;
		if (GeneratorConstants.SPECIAL_INLINE_TYPE.equals(n.getName())) {
			printer.print("var ");
			className = n.getName();
		} else {
			TypeWrapper type = resolvedType(n);
			if (!type.isInnerType()) {
				printer.print("var ");
			}
			className = stJsName(resolvedType(n));
		}
		printer.print(className);

		printer.print(" = ");
		if (n.getMembers() != null) {
			ConstructorDeclaration constr = getConstructor(n.getMembers(), context);
			if (constr != null) {
				constr.accept(this, context);
				printer.print(";");
			} else {
				printer.print("function(){");
				addCallToSuper(scope, context, Collections.<Expression> emptyList());
				printer.printLn("};");
			}

			// XXX it's not really working for multiple extends
			List<TypeWrapper> implementsOrExtends = getImplementsOrExtends(n);
			if (implementsOrExtends.size() > 0) {
				printer.printLn();
				printer.print("stjs.extend(");

				printer.print(className);

				for (TypeWrapper ext : implementsOrExtends) {
					printer.print(", " + stJsName(ext));
				}

				printer.printLn(");");
			}
			printMembers(n.getMembers(), context);
		}
		printTypeDescription(n, context);
		printMainMethodCall(n);
	}

	/**
	 * 
	 * @param typeWrapper
	 * @return the name of the given type. if the type is a parameterized type it returns {name:"type-name",
	 *         arguments:[args..]}
	 */
	private String stJsNameInfo(TypeWrapper typeWrapper) {
		// We may want to use a more complex naming scheme, to avoid conflicts across packages
		if (typeWrapper instanceof ParameterizedTypeWrapper) {
			ParameterizedTypeWrapper pt = (ParameterizedTypeWrapper) typeWrapper;
			StringBuilder s = new StringBuilder();
			s.append("{name:\"").append(pt.getExternalName()).append("\"");

			s.append(", arguments:[");
			boolean first = true;
			for (TypeWrapper arg : pt.getActualTypeArguments()) {
				if (!first) {
					s.append(",");
				}
				s.append(stJsNameInfo(arg));
				first = false;
			}
			s.append("]");
			s.append("}");
			return s.toString();
		}

		if (typeWrapper instanceof ClassWrapper && ((ClassWrapper) typeWrapper).getClazz().isEnum()) {
			StringBuilder s = new StringBuilder();
			s.append("{name:\"Enum\"");
			s.append(", arguments:[");
			s.append("\"" + stJsName(typeWrapper) + "\"");
			s.append("]");
			s.append("}");
			return s.toString();
		}
		if (ClassUtils.isBasicType(typeWrapper)) {
			return "null";
		}
		return "\"" + stJsName(typeWrapper) + "\"";
	}

	/**
	 * print the information needed to deserialize type-safe from json
	 * 
	 * @param n
	 * @param context
	 */
	private void printTypeDescription(ClassOrInterfaceDeclaration n, GenerationContext context) {
		TypeWrapper type = resolvedType(n);
		if (isGlobal(type)) {
			return;
		}
		String typeName = stJsName(type);
		printer.printLn();
		printer.print(typeName);
		printer.print(".");
		printer.print(GeneratorConstants.TYPE_DESCRIPTION_PROPERTY).print("=");

		boolean generateSuperClass = false;
		if ((n.getExtends() != null) && (n.getExtends().size() > 0)) {
			TypeWrapper superClass = resolvedType(n.getExtends().get(0));
			if (!ClassUtils.isSyntheticType(superClass)) {
				generateSuperClass = true;
				printer.print("stjs.copyProps(");

				// for (ClassOrInterfaceType ext : n.getExtends()) {
				// printer.print(", " + stJsName(resolvedType(ext)));
				// }
				printer.print(stJsName(superClass));
				printer.print(".").print(GeneratorConstants.TYPE_DESCRIPTION_PROPERTY).print(", ");
			}
		}

		printer.print("{");
		if (n.getMembers() != null) {
			boolean first = true;
			for (BodyDeclaration member : n.getMembers()) {
				if (member instanceof FieldDeclaration) {
					FieldDeclaration field = (FieldDeclaration) member;
					TypeWrapper fieldType = resolvedType(field.getType());

					if (ClassUtils.isBasicType(fieldType)) {
						continue;
					}
					for (VariableDeclarator v : field.getVariables()) {
						if (!first) {
							printer.print(", ");
						}
						printer.print("\"").print(v.getId().getName()).print("\":");
						printer.print(stJsNameInfo(fieldType));
						first = false;
					}
				}
			}
		}
		printer.print("}");
		if (generateSuperClass) {
			printer.print(")");
		}
		printer.printLn(";");
	}

	private void printMembers(List<BodyDeclaration> members, GenerationContext context) {
		for (BodyDeclaration member : members) {
			if (member instanceof ConstructorDeclaration) {
				continue;
			}
			printer.printLn();
			member.accept(this, context);
		}
	}

	private ConstructorDeclaration getConstructor(List<BodyDeclaration> members, GenerationContext context) {
		ConstructorDeclaration constr = null;
		for (BodyDeclaration member : members) {
			if (member instanceof ConstructorDeclaration) {
				if (constr != null) {
					throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(member),
							"Only maximum one constructor is allowed");
				} else {
					constr = (ConstructorDeclaration) member;
				}
			}
		}
		return constr;
	}

	private void printStaticMembersPrefix(ClassScope scope) {
		printer.print(stJsName(scope.getClazz()));
	}

	private void printMainMethodCall(ClassOrInterfaceDeclaration n) {
		if (n.getMembers() == null) {
			return;
		}
		ClassScope scope = (ClassScope) scope(n);
		List<BodyDeclaration> members = n.getMembers();
		for (BodyDeclaration member : members) {
			if (member instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration) member;
				if (NodeUtils.isMainMethod(methodDeclaration)) {
					printer.printLn();
					printer.print("if (!stjs.mainCallDisabled) ");
					printStaticMembersPrefix(scope);
					printer.print(".main();");
				}
			}
		}
	}

	@Override
	public void visit(EmptyTypeDeclaration n, GenerationContext context) {
		printJavadoc(n.getJavaDoc(), context);
		printer.print(";");
	}

	@Override
	public void visit(EnumConstantDeclaration n, GenerationContext context) {
		// the enum constants are processed within the EnumDeclaration node. So this node should not be visited
		throw new IllegalStateException("Unexpected visit in a EnumConstantDeclaration node:" + n);

	}

	@Override
	public void visit(AnnotationDeclaration n, GenerationContext context) {
		// skip

	}

	@Override
	public void visit(AnnotationMemberDeclaration n, GenerationContext context) {
		// skip

	}

	@Override
	public void visit(EmptyMemberDeclaration n, GenerationContext context) {
		printer.print(";");
	}

	@Override
	public void visit(InitializerDeclaration n, GenerationContext context) {
		// should find a way to implement these blocks. For the moment forbid them
		throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
				"Initializing blocks are not supported by Javascript");
	}

	@Override
	public void visit(JavadocComment n, GenerationContext context) {
		printer.print("/**");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

	@Override
	public void visit(PrimitiveType n, GenerationContext context) {
		throw new IllegalStateException("Unexpected visit in a PrimitiveType node:" + n);

	}

	@Override
	public void visit(VoidType n, GenerationContext context) {
		throw new IllegalStateException("Unexpected visit in a VoidType node:" + n);
	}

	@Override
	public void visit(WildcardType n, GenerationContext context) {
		throw new IllegalStateException("Unexpected visit in a WildcardType node:" + n);
	}

	@Override
	public void visit(ArrayAccessExpr n, GenerationContext context) {
		n.getName().accept(this, context);
		printer.print("[");
		n.getIndex().accept(this, context);
		printer.print("]");
	}

	@Override
	public void visit(ArrayCreationExpr n, GenerationContext context) {
		// skip the new type[][]
		n.getInitializer().accept(this, context);
	}

	@Override
	public void visit(ArrayInitializerExpr n, GenerationContext context) {
		printer.print("[");
		if (n.getValues() != null) {
			for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
				Expression expr = i.next();
				expr.accept(this, context);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}

		}
		printer.print("]");
	}

	/**
	 * 
	 * 
	 * @param n
	 * @return true if the node is a direct child following the path:
	 *         //ObjectCreationExpr/InitializerDeclaration/BlockStmt/Child
	 */
	private boolean isInlineObjectCreationChild(Node n, int upLevel) {
		return isInlineObjectCreationBlock(parent(n, upLevel));

	}

	/**
	 * @param n
	 * @return true if the node is a block statement //ObjectCreationExpr/InitializerDeclaration/BlockStmt
	 */
	private boolean isInlineObjectCreationBlock(Node n) {
		if (!(n instanceof BlockStmt)) {
			return false;
		}
		Node p = null;
		if ((p = checkParent(n, InitializerDeclaration.class)) == null) {
			return false;
		}
		if ((p = checkParent(p, ObjectCreationExpr.class)) == null) {
			return false;
		}
		return true;
	}

	@Override
	public void visit(AssignExpr n, GenerationContext context) {
		if (isInlineObjectCreationChild(n, 2)) {
			if (n.getTarget() instanceof FieldAccessExpr) {
				// in inline object creation "this." should be removed
				printer.print(((FieldAccessExpr) n.getTarget()).getField());
			} else {
				n.getTarget().accept(this, context);
			}
			printer.print(" ");
			switch (n.getOperator()) {
			case assign:
				printer.print(":");
				break;
			default:
				throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
						"Cannot have this assign operator inside an inline object creation block");
			}
			printer.print(" ");
			n.getValue().accept(this, context);
			return;
		}

		n.getTarget().accept(this, context);
		printer.print(" ");
		switch (n.getOperator()) {
		case assign:
			printer.print("=");
			break;
		case and:
			printer.print("&=");
			break;
		case or:
			printer.print("|=");
			break;
		case xor:
			printer.print("^=");
			break;
		case plus:
			printer.print("+=");
			break;
		case minus:
			printer.print("-=");
			break;
		case rem:
			printer.print("%=");
			break;
		case slash:
			printer.print("/=");
			break;
		case star:
			printer.print("*=");
			break;
		case lShift:
			printer.print("<<=");
			break;
		case rSignedShift:
			printer.print(">>=");
			break;
		case rUnsignedShift:
			printer.print(">>>=");
			break;
		}
		printer.print(" ");
		n.getValue().accept(this, context);

	}

	@Override
	public void visit(BinaryExpr n, GenerationContext context) {
		n.getLeft().accept(this, context);
		printer.print(" ");
		switch (n.getOperator()) {
		case or:
			printer.print("||");
			break;
		case and:
			printer.print("&&");
			break;
		case binOr:
			printer.print("|");
			break;
		case binAnd:
			printer.print("&");
			break;
		case xor:
			printer.print("^");
			break;
		case equals:
			printer.print("==");
			break;
		case notEquals:
			printer.print("!=");
			break;
		case less:
			printer.print("<");
			break;
		case greater:
			printer.print(">");
			break;
		case lessEquals:
			printer.print("<=");
			break;
		case greaterEquals:
			printer.print(">=");
			break;
		case lShift:
			printer.print("<<");
			break;
		case rSignedShift:
			printer.print(">>");
			break;
		case rUnsignedShift:
			printer.print(">>>");
			break;
		case plus:
			printer.print("+");
			break;
		case minus:
			printer.print("-");
			break;
		case times:
			printer.print("*");
			break;
		case divide:
			printer.print("/");
			break;
		case remainder:
			printer.print("%");
			break;
		}
		printer.print(" ");
		n.getRight().accept(this, context);
	}

	@Override
	public void visit(ClassExpr n, GenerationContext context) {
		String typeName = stJsName(resolvedType(n.getType()));
		printer.print(typeName);
		// printer.print(".prototype");
	}

	@Override
	public void visit(ConditionalExpr n, GenerationContext context) {
		n.getCondition().accept(this, context);
		printer.print(" ? ");
		n.getThenExpr().accept(this, context);
		printer.print(" : ");
		n.getElseExpr().accept(this, context);
	}

	@Override
	public void visit(EnclosedExpr n, GenerationContext context) {
		printer.print("(");
		n.getInner().accept(this, context);
		printer.print(")");
	}

	@Override
	public void visit(InstanceOfExpr n, GenerationContext context) {
		n.getExpr().accept(this, context);
		printer.print(".constructor ==  ");
		if (n.getType() instanceof ReferenceType) {
			// TODO : could be more generic
			TypeWrapper type = scope(n).resolveType(((ReferenceType) n.getType()).getType().toString()).getType();
			printer.print(stJsName(type));
		} else {
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"Do not know how to handle instanceof statement");
		}
		// n.getType().accept(this, context);
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, GenerationContext context) {
		printer.print(n.getValue());
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, GenerationContext context) {
		printer.print(n.getValue());
	}

	@Override
	public void visit(NullLiteralExpr n, GenerationContext context) {
		printer.print("null");
	}

	@Override
	public void visit(FieldAccessExpr n, GenerationContext context) {
		boolean withScopeSuper = (n.getScope() != null) && n.getScope().toString().equals(GeneratorConstants.SUPER);
		if (!withScopeSuper) {
			n.getScope().accept(this, context);
		}
		TypeWrapper scopeType = resolvedType(n.getScope());
		FieldWrapper field = (FieldWrapper) resolvedVariable(n);
		boolean skipType = (field != null) && Modifier.isStatic(field.getModifiers()) && isGlobal(scopeType);
		if ((scopeType == null) || !skipType) {
			if (withScopeSuper) {
				// super.field does not make sense, so convert it to this
				printer.print("this");
			}
			printer.print(".");
		}
		printer.print(n.getField());
	}

	@Override
	public void visit(final MethodCallExpr n, final GenerationContext context) {
		MethodWrapper method = resolvedMethod(n);
		if (!specialMethodHandlers.handleMethodCall(this, n, context)) {
			TypeWrapper methodDeclaringClass = method.getOwnerType();
			if (Modifier.isStatic(method.getModifiers())) {
				printStaticFieldOrMethodAccessPrefix(methodDeclaringClass, true);
				printer.print(n.getName());
				printArguments(n.getArgs(), context);
				return;
			}
			boolean withScopeThis = (n.getScope() != null) && n.getScope().toString().equals(GeneratorConstants.THIS);
			boolean withScopeSuper = (n.getScope() != null) && n.getScope().toString().equals(GeneratorConstants.SUPER);
			if ((n.getScope() != null) && !withScopeSuper && !withScopeThis) {
				n.getScope().accept(this, context);
				printer.print(".");
			} else if (!withScopeSuper) {
				// Non static reference to current enclosing type.
				printer.print("this.");
			} else {
				// Non static reference to parent type
				printer.print(stJsName(method.getOwnerType()));
				printer.print(".prototype.").print(n.getName()).print(".call");
				printArguments(Collections.singleton("this"), n.getArgs(), Collections.<String> emptyList(), context);
				return;
			}
			printer.print(n.getName());
			printArguments(n.getArgs(), context);

		}
	}

	private void printStaticFieldOrMethodAccessPrefix(TypeWrapper type, boolean addDot) {
		if (!isGlobal(type)) {
			printer.print(stJsName(type));
			if (addDot) {
				printer.print(".");
			}
		}
	}

	private boolean isGlobal(TypeWrapper clazz) {
		return clazz.hasAnnotation(GlobalScope.class);
	}

	@Override
	public void visit(NameExpr n, GenerationContext context) {
		if (GeneratorConstants.SPECIAL_THIS.equals(n.getName())) {
			printer.print("this");
			return;
		}
		Variable var = resolvedVariable(n);
		if (var != null) {
			if (var instanceof FieldWrapper) {
				FieldWrapper field = ((FieldWrapper) var);
				if (Modifier.isStatic(field.getModifiers())) {
					printStaticFieldOrMethodAccessPrefix(field.getOwnerType(), true);
				} else if (!isInlineObjectCreationChild(n, 3)) {
					printer.print("this.");
				}
			}
		} else if (!(parent(n) instanceof SwitchEntryStmt)) {
			TypeWrapper type = resolvedType(n);
			if (type != null) {
				printStaticFieldOrMethodAccessPrefix(type, false);
				return;
			}
		}

		printer.print(n.getName());
	}

	@Override
	public void visit(QualifiedNameExpr n, GenerationContext context) {
		n.getQualifier().accept(this, context);
		printer.print(".");
		printer.print(n.getName());
	}

	@Override
	public void visit(ThisExpr n, GenerationContext context) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, context);
			printer.print(".");
		}
		printer.print("this");
	}

	@Override
	public void visit(SuperExpr n, GenerationContext context) {
		throw new IllegalStateException("The [super] node should've been already handled:" + n);
	}

	@Override
	public void visit(UnaryExpr n, GenerationContext context) {
		switch (n.getOperator()) {
		case positive:
			printer.print("+");
			break;
		case negative:
			printer.print("-");
			break;
		case inverse:
			printer.print("~");
			break;
		case not:
			printer.print("!");
			break;
		case preIncrement:
			printer.print("++");
			break;
		case preDecrement:
			printer.print("--");
			break;
		}

		n.getExpr().accept(this, context);

		switch (n.getOperator()) {
		case posIncrement:
			printer.print("++");
			break;
		case posDecrement:
			printer.print("--");
			break;
		}
	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, GenerationContext context) {
		// skip

	}

	@Override
	public void visit(NormalAnnotationExpr n, GenerationContext context) {
		// skip

	}

	@Override
	public void visit(MemberValuePair n, GenerationContext context) {
		// skip (annotations)
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, GenerationContext context) {
		if (n.isThis()) {
			// This should not happen as another constructor is forbidden
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"Only one constructor is allowed");
		}

		ClassScope classScope = scope(n).closest(ClassScope.class);
		addCallToSuper(classScope, context, n.getArgs());
	}

	@Override
	public void visit(TypeDeclarationStmt n, GenerationContext context) {
		n.getTypeDeclaration().accept(this, context);
	}

	@Override
	public void visit(AssertStmt n, GenerationContext context) {
		throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
				"Assert statement is not supported by Javascript");
	}

	private void checkAssignStatement(Statement s, GenerationContext context) {
		if (s instanceof ExpressionStmt) {
			if (((ExpressionStmt) s).getExpression() instanceof AssignExpr) {
				return;
			}
		}
		throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(s),
				"Only assign expression are allowed in an object creation block");
	}

	private void generateArrayHasOwnProperty(ForeachStmt n, GenerationContext context) {
		if (!context.getConfiguration().isGenerateArrayHasOwnProperty()) {
			return;
		}

		TypeWrapper iterated = resolvedType(n.getIterable());

		if (!iterated.isAssignableFrom(TypeWrappers.wrap(Array.class))) {
			return;
		}
		printer.print("if (!(");
		n.getIterable().accept(this, context);
		printer.print(").hasOwnProperty(");
		printer.print(n.getVariable().getVars().get(0).getId().getName());
		printer.printLn(")) continue;");
	}

	@Override
	public void visit(BlockStmt n, GenerationContext context) {
		printer.printLn("{");
		if (n.getStmts() != null) {
			printer.indent();
			if (parent(n) instanceof ForeachStmt) {
				generateArrayHasOwnProperty((ForeachStmt) parent(n), context);
			}
			for (int i = 0; i < n.getStmts().size(); ++i) {
				Statement s = n.getStmts().get(i);
				printComments(s, context);
				if (isInlineObjectCreationChild(s, 1)) {
					checkAssignStatement(s, context);
				}
				s.accept(this, context);
				if (isInlineObjectCreationChild(s, 1) && (i < (n.getStmts().size() - 1)) && (n.getStmts().size() > 1)) {
					printer.print(",");
				}
				printer.printLn();
			}
			printer.unindent();
		}
		printer.print("}");

	}

	@Override
	public void visit(LabeledStmt n, GenerationContext context) {
		printer.print(n.getLabel());
		printer.print(": ");
		n.getStmt().accept(this, context);
	}

	@Override
	public void visit(EmptyStmt n, GenerationContext context) {
		printer.print(";");
	}

	@Override
	public void visit(ExpressionStmt n, GenerationContext context) {
		n.getExpression().accept(this, context);
		if (!isInlineObjectCreationChild(n, 1)) {
			printer.print(";");
		}
	}

	@Override
	public void visit(SwitchStmt n, GenerationContext context) {
		printer.print("switch(");
		n.getSelector().accept(this, context);
		printer.printLn(") {");
		if (n.getEntries() != null) {
			printer.indent();
			for (SwitchEntryStmt e : n.getEntries()) {
				e.accept(this, context);
			}
			printer.unindent();
		}
		printer.print("}");

	}

	@Override
	public void visit(SwitchEntryStmt n, GenerationContext context) {
		if (n.getLabel() != null) {
			printer.print("case ");
			TypeWrapper selectorType = resolvedType(((SwitchStmt) parent(n)).getSelector());
			PreConditions.checkState(selectorType != null, "The selector of the switch %s should have a type",
					parent(n));
			if ((selectorType instanceof ClassWrapper) && ((ClassWrapper) selectorType).getClazz().isEnum()) {
				printer.print(stJsName(selectorType));
				printer.print(".");
			}
			n.getLabel().accept(this, context);
			printer.print(":");
		} else {
			printer.print("default:");
		}
		printer.printLn();
		printer.indent();
		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, context);
				printer.printLn();
			}
		}
		printer.unindent();
	}

	@Override
	public void visit(BreakStmt n, GenerationContext context) {
		printer.print("break");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		printer.print(";");
	}

	@Override
	public void visit(ReturnStmt n, GenerationContext context) {
		printer.print("return");
		if (n.getExpr() != null) {
			printer.print(" ");
			n.getExpr().accept(this, context);
		}
		printer.print(";");
	}

	@Override
	public void visit(ThrowStmt n, GenerationContext context) {
		printer.print("throw ");
		n.getExpr().accept(this, context);
		printer.print(";");
	}

	@Override
	public void visit(TryStmt n, GenerationContext context) {
		printer.print("try ");
		n.getTryBlock().accept(this, context);
		if (n.getCatchs() != null) {
			for (CatchClause c : n.getCatchs()) {
				c.accept(this, context);
			}
		}
		if (n.getFinallyBlock() != null) {
			printer.print(" finally ");
			n.getFinallyBlock().accept(this, context);
		}
	}

	@Override
	public void visit(CatchClause n, GenerationContext context) {
		printer.print(" catch (");
		n.getExcept().accept(this, context);
		printer.print(") ");
		n.getCatchBlock().accept(this, context);
	}

}
