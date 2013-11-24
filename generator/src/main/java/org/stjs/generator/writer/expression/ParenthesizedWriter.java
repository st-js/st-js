package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ParenthesizedTree;

public class ParenthesizedWriter implements VisitorContributor<ParenthesizedTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ParenthesizedTree tree,
			GenerationContext p, List<AstNode> prev) {
		ParenthesizedExpression expr = new ParenthesizedExpression();
		expr.setExpression(visitor.scan(tree.getExpression(), p).get(0));
		return Collections.<AstNode>singletonList(expr);
	}
}
