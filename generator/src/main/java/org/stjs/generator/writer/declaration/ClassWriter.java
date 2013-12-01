package org.stjs.generator.writer.declaration;

import static org.stjs.generator.writer.JavaScriptNodes.functionCall;
import static org.stjs.generator.writer.JavaScriptNodes.name;
import static org.stjs.generator.writer.JavaScriptNodes.statement;
import static org.stjs.generator.writer.JavaScriptNodes.string;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
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
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class ClassWriter implements VisitorContributor<ClassTree, List<AstNode>, GenerationContext> {

	/**
	 * generate the namespace declaration stjs.ns("namespace") if needed
	 */
	private void addNamespace(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree tree, GenerationContext p,
			List<AstNode> stmts) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (JavaNodes.isInnerType(type)) {
			// this is an inner (anonymous or not) class - no namespace declaration is generated
			return;
		}
		String namespace = JavaNodes.getNamespace(type);
		if (namespace != null) {
			stmts.add(statement(functionCall(name("stjs"), "ns", string(namespace))));
		}
	}

	/**
	 * @return the node to put in the super class
	 */
	private AstNode getSuperClass(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree clazz,
			GenerationContext context) {
		Element type = TreeUtils.elementFromDeclaration(clazz);
		if (clazz.getExtendsClause() == null || type.getKind() == ElementKind.INTERFACE) {
			// no super class found
			return JavaScriptNodes.NULL();
		}

		Element superType = TreeUtils.elementFromUse((ExpressionTree) clazz.getExtendsClause());
		return name(context.getNames().getTypeName(context, superType));
	}

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

	/**
	 * add the call to the main method, if it exists
	 */
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
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ClassTree tree, GenerationContext context,
			List<AstNode> prev) {
		// if (isGlobal(type)) {
		// printGlobals(filterGlobals(n, type), context);
		// printStaticInitializers(n, context);
		// printMainMethodCall(n, type);
		// return;
		// }

		List<AstNode> stmts = new ArrayList<AstNode>();
		addNamespace(visitor, tree, context, stmts);

		// extends
		Element type = TreeUtils.elementFromDeclaration(tree);
		String typeName = context.getNames().getTypeName(context, type);
		AstNode name = name(typeName);
		AstNode superClazz = getSuperClass(visitor, tree, context);
		AstNode interfaces = JavaScriptNodes.array();
		AstNode members = getMembers(visitor, tree, context);
		AstNode typeDesc = new ObjectLiteral();
		boolean anonymousClass = (tree.getSimpleName().length() == 0);

		if (anonymousClass) {
			// anonymous class
			name = getConstructor(visitor, tree, context);
		} else if (typeName.contains(".")) {
			// inner class
			stmts.add(statement(JavaScriptNodes.assignment(null, typeName, getConstructor(visitor, tree, context))));
		} else {
			// regular class
			stmts.add(JavaScriptNodes.variableDeclarationStatement(typeName, getConstructor(visitor, tree, context)));
		}

		FunctionCall extendsCall = functionCall(name("stjs"), "extend", name, superClazz, interfaces, members, typeDesc);
		if (anonymousClass) {
			stmts.add(extendsCall);
		} else {
			stmts.add(JavaScriptNodes.statement(extendsCall));
		}
		// printNonGlobalClass(n, type, scope, context);
		// if (!type.isInnerType()) {
		// printStaticInitializers(n, context);
		addMainMethodCall(tree, stmts);
		// }

		return stmts;
	}

}
