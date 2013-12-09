package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ContinueStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.ContinueTree;

public class ContinueWriter implements VisitorContributor<ContinueTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, ContinueTree tree,
			GenerationContext context, List<AstNode> prev) {
		ContinueStatement stmt = new ContinueStatement();
		if (tree.getLabel() != null) {
			stmt.setLabel(JavaScriptNodes.name(tree.getLabel().toString()));
		}
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
