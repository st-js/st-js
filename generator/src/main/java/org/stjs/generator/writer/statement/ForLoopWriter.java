package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.EmptyExpression;
import org.mozilla.javascript.ast.ForLoop;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.Tree;

public class ForLoopWriter implements VisitorContributor<ForLoopTree, List<AstNode>, GenerationContext> {

	private void addInit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ForLoopTree tree, GenerationContext p,
			ForLoop stmt) {
		List<AstNode> jsNodes = new ArrayList<AstNode>();
		for (Tree u : tree.getInitializer()) {
			jsNodes.addAll(visitor.scan(u, p));
		}
		stmt.setInitializer(JavaScriptNodes.asExpressionList(jsNodes));
	}

	private void addCondition(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ForLoopTree tree, GenerationContext p,
			ForLoop stmt) {
		if (tree.getCondition() == null) {
			stmt.setCondition(new EmptyExpression());
		} else {
			stmt.setCondition(visitor.scan(tree.getCondition(), p).get(0));
		}
	}

	private void addUpdate(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ForLoopTree tree, GenerationContext p,
			ForLoop stmt) {
		List<AstNode> jsNodes = new ArrayList<AstNode>();
		for (ExpressionStatementTree u : tree.getUpdate()) {
			jsNodes.addAll(visitor.scan(u.getExpression(), p));
		}
		stmt.setIncrement(JavaScriptNodes.asExpressionList(jsNodes));
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ForLoopTree tree, GenerationContext context,
			List<AstNode> prev) {
		ForLoop stmt = new ForLoop();

		addInit(visitor, tree, context, stmt);
		addCondition(visitor, tree, context, stmt);
		addUpdate(visitor, tree, context, stmt);

		stmt.setBody(visitor.scan(tree.getStatement(), context).get(0));
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
