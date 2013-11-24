package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Block;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.Tree;

public class BlockWriter implements VisitorContributor<BlockTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, BlockTree tree, GenerationContext p,
			List<AstNode> prev) {
		// only for regular and static block. instance initializing blocks are not supported
		Block stmt = new Block();
		for (Tree child : tree.getStatements()) {
			List<AstNode> jsNodes = visitor.scan(child, p);
			for (AstNode jsNode : jsNodes) {
				stmt.addChild(jsNode);
			}
		}
		return Collections.<AstNode>singletonList(stmt);
	}
}
