package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.EmptyStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.EmptyStatementTree;

public class EmptyStatementWriter implements VisitorContributor<EmptyStatementTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, EmptyStatementTree tree,
			GenerationContext p, List<AstNode> prev) {
		EmptyStatement stmt = new EmptyStatement();
		return Collections.<AstNode>singletonList(stmt);
	}
}
