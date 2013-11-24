package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.AssignmentTree;

public class AssignmentWriter implements VisitorContributor<AssignmentTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, AssignmentTree tree, GenerationContext p,
			List<AstNode> prev) {
		Assignment expr = new Assignment();
		expr.setLeft(visitor.scan(tree.getVariable(), p).get(0));
		expr.setRight(visitor.scan(tree.getExpression(), p).get(0));

		expr.setOperator(Token.ASSIGN);
		return Collections.<AstNode>singletonList(expr);
	}
}
