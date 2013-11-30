package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.functionCall;
import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;
import org.stjs.generator.writer.JavascriptKeywords;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ClassWriter implements VisitorContributor<ClassTree, List<AstNode>, GenerationContext> {
	// private String printNamespace(ClassWrapper type) {
	// String namespace = null;
	// if (!ClassUtils.isInnerType(type)) {
	// namespace = ClassUtils.getNamespace(type);
	// if (namespace != null) {
	// printer.printLn("stjs.ns(\"" + namespace + "\");");
	// }
	// }
	// return namespace;
	// }

	/**
	 * @return the JavaScript node for the class' constructor
	 */
	private AstNode getConstructor(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree clazz, GenerationContext p) {
		for (Tree member : clazz.getMembers()) {
			if (JavaNodes.isConstructor(member)) {
				// TODO skip the "native" constructors
				return visitor.scan(member, p).get(0);
			}
		}
		// no constructor found -> the compiler normally generates one
		return null;
	}

	/**
	 * @return the JavaScript node for the class' members
	 */
	private AstNode getMembers(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree clazz, GenerationContext p) {
		// the following members must not appear in the initializer function:
		// - constructors (they are printed elsewhere)
		// - abstract methods (they should be omitted)

		List<Tree> nonConstructors = new ArrayList<Tree>();
		for (Tree member : clazz.getMembers()) {
			if (!JavaNodes.isConstructor(member) && !isAbstractInstanceMethod(member)) {
				nonConstructors.add(member);
			}
		}

		if (nonConstructors.isEmpty()) {
			return JavaScriptNodes.keyword(Token.NULL);
		}
		FunctionNode decl = new FunctionNode();
		decl.addParam(JavaScriptNodes.name(JavascriptKeywords.CONSTRUCTOR));
		decl.addParam(JavaScriptNodes.name(JavascriptKeywords.PROTOTYPE));

		Block body = new Block();
		for (Tree member : nonConstructors) {
			List<AstNode> jsNodes = visitor.scan(member, p);
			for (AstNode jsNode : jsNodes) {
				body.addStatement(jsNode);
			}
		}
		decl.setBody(body);

		return decl;
	}

	private boolean isAbstractInstanceMethod(Tree member) {
		if (!(member instanceof MethodTree)) {
			return false;
		}
		Set<Modifier> modifiers = ((MethodTree) member).getModifiers().getFlags();
		return modifiers.contains(Modifier.ABSTRACT) && !modifiers.contains(Modifier.STATIC);
	}

	private boolean hasMainMethod(ClassTree clazz) {
		for (Tree member : clazz.getMembers()) {
			if (!(member instanceof MethodTree)) {
				continue;
			}
			MethodTree method = (MethodTree) member;

			if (JavaNodes.isStatic(method) && "main".equals(method.getName().toString())) {
				// TODO check params isMainMethod = isStringArray(methodDeclaration.getParameters());
				return true;
			}
		}
		return false;
	}

	private void addMainMethodCall(ClassTree clazz, List<AstNode> stmts) {
		if (!hasMainMethod(clazz)) {
			return;
		}
		IfStatement ifs = new IfStatement();
		ifs.setCondition(JavaScriptNodes.not(JavaScriptNodes.property(name("stjs"), "mainCallDisabled")));
		ifs.setThenPart(statement(functionCall(name(clazz.getSimpleName()), "main")));
		stmts.add(ifs);
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree tree, GenerationContext p,
			List<AstNode> prev) {
		// if (isGlobal(type)) {
		// printGlobals(filterGlobals(n, type), context);
		// printStaticInitializers(n, context);
		// printMainMethodCall(n, type);
		// return;
		// }

		List<AstNode> stmts = new ArrayList<AstNode>();
		addTypeName(visitor, tree, p, stmts);

		// extends
		AstNode name = JavaScriptNodes.name(tree.getSimpleName());
		AstNode superClazz = JavaScriptNodes.keyword(Token.NULL);
		AstNode interfaces = JavaScriptNodes.array();
		AstNode members = getMembers(visitor, tree, p);
		AstNode typeDesc = new ObjectLiteral();

		FunctionCall extendsCall = JavaScriptNodes.functionCall(JavaScriptNodes.name("stjs"), "extend", name, superClazz, interfaces, members,
				typeDesc);
		stmts.add(JavaScriptNodes.statement(extendsCall));
		// printNonGlobalClass(n, type, scope, context);
		// if (!type.isInnerType()) {
		// printStaticInitializers(n, context);
		addMainMethodCall(tree, stmts);
		// }

		return stmts;
	}

	private void addTypeName(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree tree, GenerationContext p,
			List<AstNode> stmts) {
		if (tree.getSimpleName() == null) {
			// TODO anonymous class
			// printer.print("(");
		} else {
			stmts.add(JavaScriptNodes.variableDeclarationStatement(tree.getSimpleName().toString(), getConstructor(visitor, tree, p)));
		}
		// if (!type.isInnerType() && namespace == null) {
		// printer.print("var ");
		// }
		// className = names.getTypeName(type);
		// if (type.isInnerType()) {
		// printer.print("constructor.");
		// printer.print(type.getSimpleName());
		// } else {
		// printer.print(className);
		// }
		// printer.print(EQUALS);
		// if (!type.hasAnonymousDeclaringClass()) {
		// printConstructorImplementation(n, context, scope, type.isAnonymousClass());
		// printer.printLn(";");
		// }
		// }
	}

}
