package org.stjs.generator.writer.expression;

import java.util.ArrayList;
import java.util.List;

import javacutils.TreeUtils;

import javax.lang.model.element.ExecutableElement;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.MemberWriters;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

public class MethodInvocationWriter implements VisitorContributor<MethodInvocationTree, List<AstNode>, GenerationContext> {
	public static AstNode buildTarget(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context) {
		ExpressionTree select = tree.getMethodSelect();
		ExecutableElement methodDecl = TreeUtils.elementFromUse(tree);
		assert methodDecl != null : "Cannot find the definition for method  " + tree.getMethodSelect();

		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			return MemberWriters.buildTarget(context, methodDecl);
		}
		// calls with target: target.method(args)
		MemberSelectTree memberSelect = (MemberSelectTree) select;
		if (TreeUtils.isSuperCall(tree)) {
			// this is a call of type super.staticMethod(args) -> it should be handled as a simple call to
			// staticMethod
			return MemberWriters.buildTarget(context, methodDecl);
		}
		List<AstNode> exprNodes = visitor.scan(memberSelect.getExpression(), context);
		return exprNodes.isEmpty() ? null : exprNodes.get(0);
	}

	public static String buildMethodName(MethodInvocationTree tree) {
		ExpressionTree select = tree.getMethodSelect();
		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			return ((IdentifierTree) select).getName().toString();
		}
		// calls with target: target.method(args)
		MemberSelectTree memberSelect = (MemberSelectTree) select;
		return memberSelect.getIdentifier().toString();
	}

	public static List<AstNode> buildArguments(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context) {
		List<AstNode> arguments = new ArrayList<AstNode>();
		for (Tree arg : tree.getArguments()) {
			arguments.addAll(visitor.scan(arg, context));
		}
		return arguments;
	}

	public static String buildTemplateName(MethodInvocationTree tree, GenerationContext context) {
		ExecutableElement methodDecl = TreeUtils.elementFromUse(tree);
		String name = JavaNodes.getMethodTemplate(context.getElements(), methodDecl);
		if (name != null) {
			return name;
		}
		return JavaNodes.isJavaScriptFunction(methodDecl.getEnclosingElement()) ? "invoke" : "none";
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> prev) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(MethodInvocationWriter.class.getSimpleName(), templateName), tree, context);
	}
}
