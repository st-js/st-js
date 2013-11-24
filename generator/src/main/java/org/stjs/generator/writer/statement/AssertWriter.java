package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.AssertTree;

public class AssertWriter implements VisitorContributor<AssertTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, AssertTree tree, GenerationContext p,
			List<AstNode> prev) {
		// Java assert are not allowed
		assert true;
		return Collections.<AstNode>emptyList();
	}
}
