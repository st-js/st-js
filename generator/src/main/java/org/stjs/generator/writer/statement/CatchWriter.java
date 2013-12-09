package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.CatchClause;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.CatchTree;

public class CatchWriter implements VisitorContributor<CatchTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, CatchTree tree, GenerationContext context,
			List<AstNode> prev) {
		CatchClause stmt = new CatchClause();
		stmt.setCatchCondition(visitor.scan(tree.getParameter(), context).get(0));
		stmt.setBody((Block) visitor.scan(tree.getBlock(), context).get(0));
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
