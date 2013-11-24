package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.CatchClause;
import org.mozilla.javascript.ast.TryStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;

public class TryWriter implements VisitorContributor<TryTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, TryTree tree, GenerationContext p,
			List<AstNode> prev) {
		TryStatement stmt = new TryStatement();
		stmt.setTryBlock(visitor.scan(tree.getBlock(), p).get(0));
		for (Tree c : tree.getCatches()) {
			stmt.addCatchClause((CatchClause) visitor.scan(c, p).get(0));
		}
		if (tree.getFinallyBlock() != null) {
			stmt.setFinallyBlock(visitor.scan(tree.getFinallyBlock(), p).get(0));
		}
		return Collections.<AstNode>singletonList(stmt);
	}
}
