package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.IfStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.IfTree;

public class IfWriter implements VisitorContributor<IfTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, IfTree tree, GenerationContext context,
			List<AstNode> prev) {
		IfStatement stmt = new IfStatement();
		stmt.setCondition(visitor.scan(tree.getCondition(), context).get(0));
		stmt.setThenPart(visitor.scan(tree.getThenStatement(), context).get(0));

		if (tree.getElseStatement() != null) {
			stmt.setElsePart(visitor.scan(tree.getElseStatement(), context).get(0));
		}
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
