package org.stjs.generator.writer.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;
import org.stjs.generator.writer.MemberWriters;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

public class MethodInvocationWriter implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {

	/**
	 * super(args) -> SuperType.call(this, args)
	 */
	private List<AstNode> callToSuperConstructor(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor,
			MethodInvocationTree tree, GenerationContext context, List<AstNode> arguments) {
		if (!TreeUtils.isSuperCall(tree)) {
			return null;
		}

		Element methodElement = TreeUtils.elementFromUse(tree);
		if (JavaNodes.isStatic(methodElement)) {
			// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to staticMethod
			return null;
		}

		TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();

		String methodName;
		if (tree.getMethodSelect() instanceof IdentifierTree) {
			methodName = ((IdentifierTree) tree.getMethodSelect()).getName().toString();
		} else {
			methodName = ((MemberSelectTree) tree.getMethodSelect()).getIdentifier().toString();
		}

		// avoid useless call to super() when the super class is Object
		if (GeneratorConstants.SUPER.equals(methodName) && JavaNodes.sameRawType(typeElement.asType(), Object.class)) {
			return Collections.emptyList();
		}

		// transform it into superType.[prototype.method].call(this, args..);
		String typeName = context.getNames().getTypeName(context, typeElement);
		AstNode superType = JavaScriptNodes.name(GeneratorConstants.SUPER.equals(methodName) ? typeName : typeName + ".prototype." + methodName);

		arguments.add(0, JavaScriptNodes.keyword(Token.THIS));
		return Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(superType, "call", arguments));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		List<AstNode> arguments = new ArrayList<AstNode>();
		for (Tree arg : tree.getArguments()) {
			arguments.addAll(visitor.scan(arg, context));
		}

		ExpressionTree select = tree.getMethodSelect();
		String name = "";
		AstNode target = null;

		ExecutableElement methodDecl = TreeUtils.elementFromUse(tree);
		assert methodDecl != null : "Cannot find the definition for method  " + name;

		List<AstNode> js = null;

		js = callToSuperConstructor(visitor, tree, context, arguments);
		if (js != null) {
			return js;
		}

		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			name = ((IdentifierTree) select).getName().toString();

			target = MemberWriters.buildTarget(context, methodDecl);
		} else {
			// calls with target: target.method(args)
			MemberSelectTree memberSelect = (MemberSelectTree) select;
			name = memberSelect.getIdentifier().toString();

			if (TreeUtils.isSuperCall(tree)) {
				// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to
				// staticMethod
				target = MemberWriters.buildTarget(context, methodDecl);
			} else {
				target = visitor.scan(memberSelect.getExpression(), context).get(0);
			}
		}

		return Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(target, name, arguments));
	}
}
