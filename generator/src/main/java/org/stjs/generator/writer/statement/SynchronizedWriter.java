package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.SynchronizedTree;

public class SynchronizedWriter implements VisitorContributor<SynchronizedTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, SynchronizedTree tree,
			GenerationContext p, List<AstNode> prev) {
		// synchronized is not allowed
		assert true;
		return Collections.<AstNode>emptyList();
	}
}
