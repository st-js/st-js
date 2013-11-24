package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.DoLoop;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.DoWhileLoopTree;

public class DoWhileLoopWriter implements VisitorContributor<DoWhileLoopTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, DoWhileLoopTree tree, GenerationContext p,
			List<AstNode> prev) {
		DoLoop stmt = new DoLoop();
		stmt.setCondition(visitor.scan(tree.getCondition(), p).get(0));
		stmt.setBody(visitor.scan(tree.getStatement(), p).get(0));
		return Collections.<AstNode>singletonList(stmt);
	}
}
