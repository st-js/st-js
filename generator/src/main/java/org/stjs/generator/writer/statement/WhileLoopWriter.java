package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.WhileLoop;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.WhileLoopTree;

public class WhileLoopWriter implements VisitorContributor<WhileLoopTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, WhileLoopTree tree, GenerationContext context,
			List<AstNode> prev) {
		WhileLoop stmt = new WhileLoop();
		stmt.setCondition(visitor.scan(tree.getCondition(), context).get(0));
		stmt.setBody(visitor.scan(tree.getStatement(), context).get(0));
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
