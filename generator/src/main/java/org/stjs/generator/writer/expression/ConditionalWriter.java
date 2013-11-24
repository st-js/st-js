package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ConditionalExpressionTree;

public class ConditionalWriter implements VisitorContributor<ConditionalExpressionTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ConditionalExpressionTree tree,
			GenerationContext p, List<AstNode> prev) {
		ConditionalExpression expr = new ConditionalExpression();
		expr.setTestExpression(visitor.scan(tree.getCondition(), p).get(0));
		expr.setTrueExpression(visitor.scan(tree.getTrueExpression(), p).get(0));
		expr.setFalseExpression(visitor.scan(tree.getFalseExpression(), p).get(0));
		return Collections.<AstNode>singletonList(expr);
	}
}
