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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.stjs.generator.ASTNodeData;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.SourcePosition;
import org.stjs.generator.scope.JavaTypeName;
import org.stjs.generator.scope.NameResolverVisitor;
import org.stjs.generator.scope.NameScope;
import org.stjs.generator.scope.NameType.IdentifierName;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.NameType.TypeName;
import org.stjs.generator.scope.ParentTypeScope;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.TypeScope;
import org.stjs.generator.utils.Option;
import org.stjs.generator.utils.PreConditions;

/**
 * This class visits the AST corresponding to a Java file and generates the corresponding Javascript code. It presumes
 * the {@link NameResolverVisitor} previously visited the tree and set the resolved name of certain nodes.
 * 
 * @author acraciun
 */
public class GeneratorVisitor implements VoidVisitor<GenerationContext> {
	private final SpecialMethodHandlers specialMethodHandlers;

	private final boolean generateMainMethodCall;
	JavascriptWriter printer = new JavascriptWriter();

	private List<Comment> comments;

	private int currentComment = 0;

	public GeneratorVisitor(boolean generateMainMethodCall, Set<String> adapterClassNames) {
		specialMethodHandlers = new SpecialMethodHandlers(adapterClassNames);
		this.generateMainMethodCall = generateMainMethodCall;
	}

	public String getGeneratedSource() {
		return printer.getSource();
	}

	@Override
	public void visit(CompilationUnit n, GenerationContext arg) {
		comments = n.getComments();
		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
				i.next().accept(this, arg);
				printer.printLn();
				if (i.hasNext()) {
					printer.printLn();
				}
			}
		}
	}

	@Override
	public void visit(ClassOrInterfaceType n, GenerationContext arg) {
		QualifiedName<IdentifierName> qname = arg.resolveIdentifier(n);
		if ((qname != null) && (qname.getDefinitionPoint().isDefined())) {
			printer.print(qname.getDefinitionPoint().getOrNull().getFullName(false).getOrThrow());
		} else {
			if (n.getScope() != null) {
				n.getScope().accept(this, arg);
				printer.print(".");
			}
			printer.print(n.getName());
		}
	}

	@Override
	public void visit(ReferenceType n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(ImportDeclaration n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(PackageDeclaration n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(MarkerAnnotationExpr n, GenerationContext arg) {
		// skip
	}

	@Override
	public void visit(SynchronizedStmt n, GenerationContext arg) {
		throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
				"synchronized blocks are not supported by Javascript");
	}

	@Override
	public void visit(CastExpr n, GenerationContext arg) {
		// skip to cast type - continue with the expression
		if (n.getExpr() != null) {
			n.getExpr().accept(this, arg);
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
	public void visit(StringLiteralExpr n, GenerationContext arg) {
		printer.printStringLiteral(n.getValue());
	}

	@Override
	public void visit(CharLiteralExpr n, GenerationContext arg) {
		printer.printCharLiteral(n.getValue());
	}

	@Override
	public void visit(DoubleLiteralExpr n, GenerationContext arg) {
		printer.printNumberLiteral(n.getValue());
	}

	@Override
	public void visit(BooleanLiteralExpr n, GenerationContext arg) {
		printer.printLiteral(Boolean.toString(n.getValue()));
	}

	public void print(StringLiteralExpr n) {
		// java has some more syntax to declare integers :
		// 0x0, 0b0, (java7) 1_000_000
		// TxODO : convert it to plain numbers for javascript
		printer.printLiteral(n.getValue());
	}

	@Override
	public void visit(EnumDeclaration n, GenerationContext arg) {
		printComments(n, arg);
		// printer.print(n.getName());
		printStaticMembersPrefix(n, arg);
		// TxODO implements not considered
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
		// TxODO members not considered
		printer.printLn("");
		printer.unindent();
		printer.print(");");
	}

	@Override
	public void visit(ForeachStmt n, GenerationContext arg) {
		printer.print("for (");
		n.getVariable().accept(this, arg);
		printer.print(" in ");
		n.getIterable().accept(this, arg);
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	@Override
	public void visit(IfStmt n, GenerationContext arg) {
		printer.print("if (");
		n.getCondition().accept(this, arg);
		printer.print(") ");
		n.getThenStmt().accept(this, arg);
		if (n.getElseStmt() != null) {
			printer.print(" else ");
			n.getElseStmt().accept(this, arg);
		}
	}

	@Override
	public void visit(WhileStmt n, GenerationContext arg) {
		printer.print("while (");
		n.getCondition().accept(this, arg);
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	@Override
	public void visit(ContinueStmt n, GenerationContext arg) {
		printer.print("continue");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		printer.print(";");
	}

	@Override
	public void visit(DoStmt n, GenerationContext arg) {
		printer.print("do ");
		n.getBody().accept(this, arg);
		printer.print(" while (");
		n.getCondition().accept(this, arg);
		printer.print(");");
	}

	@Override
	public void visit(ForStmt n, GenerationContext arg) {
		printer.print("for (");
		if (n.getInit() != null) {
			for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print("; ");
		if (n.getCompare() != null) {
			n.getCompare().accept(this, arg);
		}
		printer.print("; ");
		if (n.getUpdate() != null) {
			for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	@Override
	public void visit(VariableDeclaratorId n, GenerationContext arg) {
		printer.print(n.getName());
	}

	@Override
	public void visit(VariableDeclarator n, GenerationContext arg) {
		throw new IllegalStateException("Unexpected visit in a VariableDeclarator node:" + n);
	}

	private void printVariableDeclarator(VariableDeclarator n, GenerationContext arg, boolean forceInitNull) {
		n.getId().accept(this, arg);
		if (n.getInit() != null) {
			printer.print(" = ");
			n.getInit().accept(this, arg);
		} else if (forceInitNull) {
			printer.print(" = null");
		}

	}

	@Override
	public void visit(VariableDeclarationExpr n, GenerationContext arg) {
		// skip type
		printer.print("var ");

		for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
			VariableDeclarator v = i.next();
			printVariableDeclarator(v, arg, false);
			if (i.hasNext()) {
				printer.print(", ");
			}
		}
	}

	@Override
	public void visit(FieldDeclaration n, GenerationContext arg) {
		PreConditions.checkNotNull(arg.getCurrentType());

		// skip type
		for (VariableDeclarator v : n.getVariables()) {
			// i need prefix here!!
			printStaticMembersPrefix(arg.getCurrentType(), arg);
			if (!isStatic(n.getModifiers())) {
				printer.print(".prototype");
			}
			printer.print(".");

			printVariableDeclarator(v, arg, true);
			printer.print(";");
		}
	}

	private void printJavadoc(JavadocComment javadoc, GenerationContext arg) {
		if (javadoc != null) {
			javadoc.accept(this, arg);
		}
	}

	private void printComments(Node n, GenerationContext arg) {
		if (comments == null) {
			return;
		}
		// the problem is that the comments are all attached to the root node
		// so this method will display all the comments before the given node.
		while (currentComment < comments.size()) {
			if (comments.get(currentComment).getBeginLine() < n.getBeginLine()) {
				comments.get(currentComment).accept(this, arg);
			} else {
				break;
			}
			currentComment++;
		}
	}

	private void printMethod(String name, List<Parameter> parameters, int modifiers, BlockStmt body,
			GenerationContext arg, boolean anonymous) {

		if (ModifierSet.isAbstract(modifiers) || ModifierSet.isNative(modifiers)) {
			return;
		}

		if (anonymous) {
			printer.print("function");
		} else {
			printStaticMembersPrefix(arg.getCurrentType(), arg);
			if (!isStatic(modifiers)) {
				printer.print(".prototype");
			}
			printer.print(".");
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
				p.accept(this, arg);
				first = false;
			}
		}
		printer.print(")");
		// skip throws
		if (body == null) {
			printer.print("{}");
		} else {
			printer.print(" ");
			body.accept(this, arg);
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

	void printArguments(List<Expression> args, GenerationContext arg) {
		printArguments(Collections.<String> emptyList(), args, Collections.<String> emptyList(), arg);
	}

	private void printArguments(Collection<String> beforeParams, Collection<Expression> args,
			Collection<String> afterParams, GenerationContext arg) {
		printer.print("(");
		boolean first = true;
		for (String param : beforeParams) {
			if (!first) {
				printer.print(", ");
			}
			printer.print(param);
			first = false;
		}
		if (args != null) {
			for (Expression e : args) {
				if (!first) {
					printer.print(", ");
				}
				e.accept(this, arg);
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

	private ClassOrInterfaceDeclaration buildClassDeclaration(String className, String extendsFrom,
			List<BodyDeclaration> members, List<Expression> constructorArgs) {
		ClassOrInterfaceDeclaration decl = new ClassOrInterfaceDeclaration();
		decl.setName(className);
		decl.setExtends(Collections.singletonList(new ClassOrInterfaceType(extendsFrom)));
		decl.setMembers(members);
		// TODO add constructor if needed to call the super with the constructorArgs
		return decl;
	}

	@Override
	public void visit(ObjectCreationExpr n, GenerationContext arg) {
		InitializerDeclaration block = getInitializerDeclaration(n);
		if (block != null) {
			// special construction for object initialization new Object(){{x = 1; y = 2; }};
			block.getBlock().accept(this, arg);
			return;
		}

		if ((n.getAnonymousClassBody() != null) && (n.getAnonymousClassBody().size() >= 1)) {
			// special construction for inline function definition
			MethodDeclaration method = getMethodDeclaration(n);
			if (method != null) {
				printMethod(method.getName(), method.getParameters(), method.getModifiers(), method.getBody(), arg,
						true);
				return;
			}
			// special construction to handle the inline body
			// build a special type called _InlineType to handle this
			printer.printLn("(function(){");
			ClassOrInterfaceDeclaration inlineFakeClass = buildClassDeclaration(GeneratorConstants.SPECIAL_INLINE_TYPE,
					n.getType().getName(), n.getAnonymousClassBody(), n.getArgs());
			inlineFakeClass.setData(n.getData());
			inlineFakeClass.accept(this, arg);

			printer.printLn("");
			printer.print("return new ").print(GeneratorConstants.SPECIAL_INLINE_TYPE);
			printArguments(n.getArgs(), arg);
			printer.printLn(";");
			printer.print("})()");
			return;
		}

		QualifiedName<TypeName> qname = arg.resolveType(n.getType());
		if ((qname != null) && qname.isDataType()) {
			// this is a call to an mock type
			printer.print("{}");
			return;
		}
		printer.print("new ");
		n.getType().accept(this, arg);
		printArguments(n.getArgs(), arg);

	}

	@Override
	public void visit(Parameter n, GenerationContext arg) {
		// skip type
		n.getId().accept(this, arg);
	}

	@Override
	public void visit(MethodDeclaration n, GenerationContext arg) {
		printComments(n, arg);
		printMethod(n.getName(), n.getParameters(), n.getModifiers(), n.getBody(), arg, false);
	}

	@Override
	public void visit(ConstructorDeclaration n, GenerationContext arg) {
		printComments(n, arg);
		printMethod(n.getName(), n.getParameters(), n.getModifiers(), n.getBlock(), arg, true);
	}

	@Override
	public void visit(TypeParameter n, GenerationContext arg) {
		// skip

	}

	@Override
	public void visit(LineComment n, GenerationContext arg) {
		printer.print("//");
		if (n.getContent().endsWith("\n")) {
			// remove trailing enter and printLn
			// to keep indentation
			printer.printLn(n.getContent().substring(0, n.getContent().length() - 1));
		}

	}

	@Override
	public void visit(BlockComment n, GenerationContext arg) {
		printer.print("/*");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, GenerationContext arg) {
		printComments(n, arg);
		// printer.print(n.getName() + " = ");
		if (GeneratorConstants.SPECIAL_INLINE_TYPE.equals(n.getName())) {
			printer.print("var ");
		} else {
			TypeScope typeScope = ((ASTNodeData) n.getData()).getTypeScope();
			JavaTypeName declaredClassName = typeScope.getDeclaredTypeName();
			if (!declaredClassName.isSubtype()) {
				printer.print("var ");
			}
		}

		printStaticMembersPrefix(n, arg);

		printer.print(" = ");
		if (n.getMembers() != null) {
			ClassOrInterfaceDeclaration prevType = arg.setCurrentType(n);
			ConstructorDeclaration constr = getConstructor(n.getMembers(), arg);
			if (constr != null) {
				constr.accept(this, arg);
				printer.print(";");
			} else {
				printer.printLn("function(){};");
			}

			if ((n.getExtends() != null) && (n.getExtends().size() > 0)) {
				printer.printLn();
				printer.print("stjs.extend(");

				printStaticMembersPrefix(n, arg);

				// TODO extends should also be with full qualifier
				printer.printLn(", " + n.getExtends().get(0).getName() + ");");
			}
			printMembers(n.getMembers(), arg);
			printMainMethodCall(n, arg);
			arg.setCurrentType(prevType);
		}

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

	private void printStaticMembersPrefix(TypeDeclaration n, GenerationContext context) {
		if (GeneratorConstants.SPECIAL_INLINE_TYPE.equals(n.getName())) {
			printer.print(n.getName());
			return;
		}
		TypeScope typeScope = ((ASTNodeData) n.getData()).getTypeScope();
		JavaTypeName declaredClassName = typeScope.getDeclaredTypeName();
		Option<String> fullyQualifiedString = declaredClassName.getFullName(false);
		if (fullyQualifiedString.isEmpty()) {
			throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
					"definition of static members of anonymous classes is not supported");
		}
		printer.print(fullyQualifiedString.getOrThrow());
	}

	private void printMainMethodCall(ClassOrInterfaceDeclaration n, GenerationContext context) {
		if (!generateMainMethodCall) {
			return;
		}
		List<BodyDeclaration> members = n.getMembers();
		for (BodyDeclaration member : members) {
			if (member instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration) member;
				if (isMainMethod(methodDeclaration)) {
					printer.printLn();
					printStaticMembersPrefix(n, context);
					printer.print(".main();");
				}
			}
		}
	}

	private boolean isMainMethod(MethodDeclaration methodDeclaration) {
		boolean isMainMethod = false;
		if (isStatic(methodDeclaration.getModifiers()) && "main".equals(methodDeclaration.getName())) {
			List<Parameter> parameters = methodDeclaration.getParameters();
			if ((parameters != null) && (parameters.size() == 1)) {
				Parameter parameter = parameters.get(0);
				if (parameter.getType() instanceof ReferenceType) {
					ReferenceType refType = (ReferenceType) parameter.getType();
					if ((refType.getArrayCount() == 1) && (refType.getType() instanceof ClassOrInterfaceType)) {
						String typeName = ((ClassOrInterfaceType) refType.getType()).getName();
						if ("String".equals(typeName) || "java.lang.String".equals(typeName)) {
							isMainMethod = true;
						}
					}
				}
			}
		}
		return isMainMethod;
	}

	@Override
	public void visit(EmptyTypeDeclaration n, GenerationContext arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printer.print(";");
	}

	@Override
	public void visit(EnumConstantDeclaration n, GenerationContext arg) {
		// the enum constants are processed within the EnumDeclaration node. So this node should not be visited
		throw new IllegalStateException("Unexpected visit in a EnumConstantDeclaration node:" + n);

	}

	@Override
	public void visit(AnnotationDeclaration n, GenerationContext arg) {
		// skip

	}

	@Override
	public void visit(AnnotationMemberDeclaration n, GenerationContext arg) {
		// skip

	}

	@Override
	public void visit(EmptyMemberDeclaration n, GenerationContext arg) {
		printer.print(";");
	}

	@Override
	public void visit(InitializerDeclaration n, GenerationContext arg) {
		// should find a way to implement these blocks. For the moment forbid them
		throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
				"Initializing blocks are not supported by Javascript");
	}

	@Override
	public void visit(JavadocComment n, GenerationContext arg) {
		printer.print("/**");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

	@Override
	public void visit(PrimitiveType n, GenerationContext arg) {
		throw new IllegalStateException("Unexpected visit in a PrimitiveType node:" + n);

	}

	@Override
	public void visit(VoidType n, GenerationContext arg) {
		throw new IllegalStateException("Unexpected visit in a VoidType node:" + n);
	}

	@Override
	public void visit(WildcardType n, GenerationContext arg) {
		throw new IllegalStateException("Unexpected visit in a WildcardType node:" + n);
	}

	@Override
	public void visit(ArrayAccessExpr n, GenerationContext arg) {
		n.getName().accept(this, arg);
		printer.print("[");
		n.getIndex().accept(this, arg);
		printer.print("]");
	}

	@Override
	public void visit(ArrayCreationExpr n, GenerationContext arg) {
		// skip the new type[][]
		n.getInitializer().accept(this, arg);
	}

	@Override
	public void visit(ArrayInitializerExpr n, GenerationContext arg) {
		printer.print("[");
		if (n.getValues() != null) {
			for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
				Expression expr = i.next();
				expr.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}

		}
		printer.print("]");
	}

	private Node parent(Node n) {
		return ((ASTNodeData) n.getData()).getParent();
	}

	private Node parent(Node n, int upLevel) {
		Node p = n;
		for (int i = 0; (i < upLevel) && (p != null); ++i) {
			p = parent(p);
		}
		return p;
	}

	private Node checkParent(Node n, Class<?> clazz) {
		Node parent = parent(n);
		if (parent == null) {
			return null;
		}
		return (clazz.isAssignableFrom(parent.getClass())) ? parent : null;
	}

	/**
	 * TODO - this can be done more generically
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
	public void visit(AssignExpr n, GenerationContext arg) {
		if (isInlineObjectCreationChild(n, 2)) {
			if (n.getTarget() instanceof FieldAccessExpr) {
				// in inline object creation "this." should be removed
				printer.print(((FieldAccessExpr) n.getTarget()).getField());
			} else {
				n.getTarget().accept(this, arg);
			}
			printer.print(" ");
			switch (n.getOperator()) {
			case assign:
				printer.print(":");
				break;
			default:
				throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
						"Cannot have this assign operator inside an inline object creation block");
			}
			printer.print(" ");
			n.getValue().accept(this, arg);
			return;
		}

		n.getTarget().accept(this, arg);
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
		n.getValue().accept(this, arg);

	}

	@Override
	public void visit(BinaryExpr n, GenerationContext arg) {
		n.getLeft().accept(this, arg);
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
		n.getRight().accept(this, arg);
	}

	@Override
	public void visit(ClassExpr n, GenerationContext arg) {
		n.getType().accept(this, arg);
		printer.print(".prototype");
	}

	@Override
	public void visit(ConditionalExpr n, GenerationContext arg) {
		n.getCondition().accept(this, arg);
		printer.print(" ? ");
		n.getThenExpr().accept(this, arg);
		printer.print(" : ");
		n.getElseExpr().accept(this, arg);
	}

	@Override
	public void visit(EnclosedExpr n, GenerationContext arg) {
		printer.print("(");
		n.getInner().accept(this, arg);
		printer.print(")");
	}

	@Override
	public void visit(InstanceOfExpr n, GenerationContext arg) {
		n.getExpr().accept(this, arg);
		printer.print(" instanceof ");
		n.getType().accept(this, arg);
	}

	@Override
	public void visit(IntegerLiteralMinValueExpr n, GenerationContext arg) {
		printer.print(n.getValue());
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, GenerationContext arg) {
		printer.print(n.getValue());
	}

	@Override
	public void visit(NullLiteralExpr n, GenerationContext arg) {
		printer.print("null");
	}

	@Override
	public void visit(FieldAccessExpr n, GenerationContext arg) {
		n.getScope().accept(this, arg);
		printer.print(".");
		printer.print(n.getField());
	}

	@Override
	public void visit(final MethodCallExpr n, final GenerationContext arg) {
		QualifiedName<MethodName> qname = arg.resolveMethod(n);
		if (!specialMethodHandlers.handleMethodCall(this, n, qname, arg)) {
			if (qname != null) {
				if (qname.isStatic()) {
					printStaticFieldOrMethodAccessPrefix(n, arg, qname, true);
					printer.print(n.getName());
					printArguments(n.getArgs(), arg);
					return;
				} else {
					if (qname.getScope() != null) {
						Boolean stopVisit = qname.getScope().visit(new NameScope.EmptyNameScopeVisitor<Boolean>(null) {
							@Override
							public Boolean caseTypeScope(TypeScope typeScope) {
								// Non static reference to current enclosing type.
								printer.print("this.");
								return false;
							}

							@Override
							public Boolean caseParentTypeScope(ParentTypeScope parentTypeScope) {
								// Non static reference to parent type
								printer.print("this._super");
								printArguments(Collections.singleton("\"" + n.getName() + "\""), n.getArgs(),
										Collections.<String> emptyList(), arg);
								return true;
							}
						});
						if (Boolean.TRUE.equals(stopVisit)) {
							return;
						}
						if (stopVisit == null) {
							if (n.getScope() != null) {
								n.getScope().accept(this, arg);
								printer.print(".");
							}
						}
						printer.print(n.getName());
						printArguments(n.getArgs(), arg);
					}
				}
			} else {
				if (n.getScope() != null) {
					n.getScope().accept(this, arg);
					printer.print(".");
				}
				printer.print(n.getName());
				printArguments(n.getArgs(), arg);
			}
		}
	}

	private void printStaticFieldOrMethodAccessPrefix(final Node n, final GenerationContext context,
			final QualifiedName<?> qname, boolean addDot) {
		if (!qname.isGlobal()) {
			JavaTypeName definitionPoint = qname.getDefinitionPoint().getOrThrow();
			if (definitionPoint.isAnonymous()) {
				throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
						"Cannot generate static field access for anonymous class"); // I think that this is not possible
																					// in Java (static field in
																					// anonymous class)
			}
			printer.print(definitionPoint.getFullName(false).getOrThrow());
			if (addDot) {
				printer.print(".");
			}
		}
	}

	@Override
	public void visit(NameExpr n, GenerationContext arg) {
		if (GeneratorConstants.SPECIAL_THIS.equals(n.getName())) {
			printer.print("this");
			return;
		}
		QualifiedName<IdentifierName> qname = arg.resolveIdentifier(n);
		if (qname != null) {
			if (qname.isStatic()) {
				printStaticFieldOrMethodAccessPrefix(n, arg, qname, true);
			} else {
				qname.getScope().visit(new NameScope.EmptyVoidNameScopeVisitor(false) {
					@Override
					public void caseTypeScope(TypeScope typeScope) {
						printer.print("this.");
					}

					@Override
					public void caseParentTypeScope(ParentTypeScope parentTypeScope) {
						// prefix with this fields that are accesses directly from the super class
						printer.print("this.");
					}
				});
			}
		}

		printer.print(n.getName());
	}

	@Override
	public void visit(QualifiedNameExpr n, GenerationContext arg) {
		n.getQualifier().accept(this, arg);
		printer.print(".");
		printer.print(n.getName());
	}

	@Override
	public void visit(ThisExpr n, GenerationContext arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
			printer.print(".");
		}
		printer.print("this");
	}

	@Override
	public void visit(SuperExpr n, GenerationContext arg) {
		throw new IllegalStateException("The [super] node should've been already handled:" + n);
	}

	@Override
	public void visit(UnaryExpr n, GenerationContext arg) {
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

		n.getExpr().accept(this, arg);

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
	public void visit(SingleMemberAnnotationExpr n, GenerationContext arg) {
		// skip

	}

	@Override
	public void visit(NormalAnnotationExpr n, GenerationContext arg) {
		// skip

	}

	@Override
	public void visit(MemberValuePair n, GenerationContext arg) {
		// XXX: not very sure when this occurs
		printer.print(n.getName());
		printer.print(" = ");
		n.getValue().accept(this, arg);
	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, GenerationContext arg) {
		if (n.isThis()) {
			// This should not happen as another constructor is forbidden
			throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
					"Only one constructor is allowed");
		}

		ClassOrInterfaceDeclaration currentTypeDecl = arg.getCurrentType();
		PreConditions.checkNotNull(currentTypeDecl);
		if ((currentTypeDecl.getExtends() != null) && (currentTypeDecl.getExtends().size() > 0)) {
			// avoid useless call to super() when the super class is Object
			printer.print("this._super");
			printArguments(Collections.singleton("null"), n.getArgs(), Collections.<String> emptyList(), arg);
			printer.print(";");
		}
	}

	@Override
	public void visit(TypeDeclarationStmt n, GenerationContext arg) {
		n.getTypeDeclaration().accept(this, arg);
	}

	@Override
	public void visit(AssertStmt n, GenerationContext arg) {
		throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(n),
				"Assert statement is not supported by Javascript");
	}

	private void checkAssignStatement(Statement s, GenerationContext arg) {
		if (s instanceof ExpressionStmt) {
			if (((ExpressionStmt) s).getExpression() instanceof AssignExpr) {
				return;
			}
		}
		throw new JavascriptGenerationException(arg.getInputFile(), new SourcePosition(s),
				"Only assign expression are allowed in an object creation block");
	}

	@Override
	public void visit(BlockStmt n, GenerationContext arg) {
		printer.printLn("{");
		if (n.getStmts() != null) {
			printer.indent();
			for (int i = 0; i < n.getStmts().size(); ++i) {
				Statement s = n.getStmts().get(i);
				printComments(s, arg);
				if (isInlineObjectCreationChild(s, 1)) {
					checkAssignStatement(s, arg);
				}
				s.accept(this, arg);
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
	public void visit(LabeledStmt n, GenerationContext arg) {
		printer.print(n.getLabel());
		printer.print(": ");
		n.getStmt().accept(this, arg);
	}

	@Override
	public void visit(EmptyStmt n, GenerationContext arg) {
		printer.print(";");
	}

	@Override
	public void visit(ExpressionStmt n, GenerationContext arg) {
		n.getExpression().accept(this, arg);
		if (!isInlineObjectCreationChild(n, 1)) {
			printer.print(";");
		}
	}

	@Override
	public void visit(SwitchStmt n, GenerationContext arg) {
		printer.print("switch(");
		n.getSelector().accept(this, arg);
		printer.printLn(") {");
		if (n.getEntries() != null) {
			printer.indent();
			for (SwitchEntryStmt e : n.getEntries()) {
				e.accept(this, arg);
			}
			printer.unindent();
		}
		printer.print("}");

	}

	@Override
	public void visit(SwitchEntryStmt n, GenerationContext arg) {
		if (n.getLabel() != null) {
			printer.print("case ");
			for (JavaTypeName enumType : getSwitchEntryEnumClassIfExists(n)) {
				printer.print(enumType.getFullName(false).getOrThrow());
				printer.print(".");
			}
			n.getLabel().accept(this, arg);
			printer.print(":");
		} else {
			printer.print("default:");
		}
		printer.printLn();
		printer.indent();
		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
				printer.printLn();
			}
		}
		printer.unindent();
	}

	private Option<JavaTypeName> getSwitchEntryEnumClassIfExists(SwitchEntryStmt n) {
		ASTNodeData nodeData = (ASTNodeData) n.getData();
		SwitchStmt switchStatement = (SwitchStmt) nodeData.getParent();
		ASTNodeData selectorData = (ASTNodeData) switchStatement.getSelector().getData();
		QualifiedName<?> qualifiedName = selectorData.getQualifiedName();
		if (qualifiedName != null) {
			for (JavaTypeName type : qualifiedName.getDefinitionPoint()) {
				// TODO if (type.isEnum()?)
				return qualifiedName.getDefinitionPoint();
			}
		}
		// TODO : switch on native types
		// TODO : not resolved, add generation error (still need to
		return Option.none();
	}

	@Override
	public void visit(BreakStmt n, GenerationContext arg) {
		printer.print("break");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		printer.print(";");
	}

	@Override
	public void visit(ReturnStmt n, GenerationContext arg) {
		printer.print("return");
		if (n.getExpr() != null) {
			printer.print(" ");
			n.getExpr().accept(this, arg);
		}
		printer.print(";");
	}

	@Override
	public void visit(ThrowStmt n, GenerationContext arg) {
		printer.print("throw ");
		n.getExpr().accept(this, arg);
		printer.print(";");
	}

	@Override
	public void visit(TryStmt n, GenerationContext arg) {
		printer.print("try ");
		n.getTryBlock().accept(this, arg);
		if (n.getCatchs() != null) {
			for (CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {
			printer.print(" finally ");
			n.getFinallyBlock().accept(this, arg);
		}
	}

	@Override
	public void visit(CatchClause n, GenerationContext arg) {
		printer.print(" catch (");
		n.getExcept().accept(this, arg);
		printer.print(") ");
		n.getCatchBlock().accept(this, arg);
	}

}
