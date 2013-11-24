package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ExpressionStatementTree;

public class ExpressionStatementWriter implements VisitorContributor<ExpressionStatementTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ExpressionStatementTree tree,
			GenerationContext p, List<AstNode> prev) {
		List<AstNode> expressions = visitor.scan(tree.getExpression(), p);
		if (expressions.isEmpty()) {
			return Collections.emptyList();
		}
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expressions.get(0));
		return Collections.<AstNode>singletonList(stmt);
	}
}
