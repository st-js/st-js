package org.stjs.generator.writer.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javacutils.TreeUtils;

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

	private List<AstNode> callToSuper(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MethodInvocationTree tree,
			GenerationContext context, List<AstNode> arguments) {
		TypeElement typeElement = context.getTrees().getScope(context.getCurrentPath()).getEnclosingClass();
		// avoid useless call to super() when the super class is Object
		if (JavaNodes.sameRawType(typeElement.getSuperclass(), Object.class)) {
			return Collections.emptyList();
		}

		// transform it into superType.apply(this, args..);
		AstNode superType = JavaScriptNodes.name(typeElement.getSuperclass().toString());

		arguments.add(0, JavaScriptNodes.keyword(Token.THIS));
		return Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(superType, "apply", arguments));
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

		if (select instanceof IdentifierTree) {
			name = ((IdentifierTree) select).getName().toString();

			if (name.equals(GeneratorConstants.SUPER)) {
				return callToSuper(visitor, tree, context, arguments);
			}
			target = MemberWriters.buildTarget(context, methodDecl);
		} else {
			MemberSelectTree memberSelect = (MemberSelectTree) select;
			name = memberSelect.getIdentifier().toString();

			target = visitor.scan(memberSelect.getExpression(), context).get(0);
		}

		return Collections.<AstNode>singletonList(JavaScriptNodes.functionCall(target, name, arguments));
	}
}
