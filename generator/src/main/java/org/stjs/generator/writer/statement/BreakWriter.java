package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.BreakStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.BreakTree;

public class BreakWriter implements VisitorContributor<BreakTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, BreakTree tree, GenerationContext context,
			List<AstNode> prev) {
		BreakStatement stmt = new BreakStatement();
		if (tree.getLabel() != null) {
			stmt.setBreakLabel(JavaScriptNodes.name(tree.getLabel().toString()));
		}
		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
