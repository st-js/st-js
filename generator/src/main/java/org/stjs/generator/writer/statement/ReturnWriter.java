package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ReturnStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ReturnTree;

public class ReturnWriter implements VisitorContributor<ReturnTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ReturnTree tree,
			GenerationContext context, List<AstNode> prev) {
		ReturnStatement stmt = new ReturnStatement();
		if (tree.getExpression() != null) {
			stmt.setReturnValue(visitor.scan(tree.getExpression(), context).get(0));
		}
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
