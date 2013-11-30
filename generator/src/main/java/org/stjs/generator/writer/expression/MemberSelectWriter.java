package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.MemberSelectTree;

public class MemberSelectWriter implements VisitorContributor<MemberSelectTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, MemberSelectTree tree,
			GenerationContext p, List<AstNode> prev) {
		// this is only for fields. Methods are handled in MethodInvocationWriter

		// TODO fix Global
		// boolean skipType = field != null && Modifier.isStatic(field.getModifiers()) && isGlobal(scopeType);

		AstNode target = null;
		if (JavaNodes.isSuper(tree.getExpression())) {
			// super.field does not make sense, so convert it to this
			target = JavaScriptNodes.THIS();
		} else {
			target = visitor.scan(tree.getExpression(), p).get(0);
		}

		return Collections.<AstNode>singletonList(JavaScriptNodes.property(target, tree.getIdentifier().toString()));
	}
}
