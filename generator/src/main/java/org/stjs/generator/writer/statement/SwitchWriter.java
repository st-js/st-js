package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.SwitchStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree;

public class SwitchWriter implements VisitorContributor<SwitchTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, SwitchTree tree, GenerationContext p,
			List<AstNode> prev) {
		SwitchStatement stmt = new SwitchStatement();
		stmt.setExpression(visitor.scan(tree.getExpression(), p).get(0));
		for (Tree c : tree.getCases()) {
			stmt.addCase((SwitchCase) visitor.scan(c, p).get(0));
		}

		return Collections.<AstNode>singletonList(stmt);
	}
}
