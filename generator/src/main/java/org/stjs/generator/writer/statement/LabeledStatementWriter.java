package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Label;
import org.mozilla.javascript.ast.LabeledStatement;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.LabeledStatementTree;

public class LabeledStatementWriter implements VisitorContributor<LabeledStatementTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, LabeledStatementTree tree,
			GenerationContext p, List<AstNode> prev) {
		LabeledStatement stmt = new LabeledStatement();
		Label label = new Label();
		label.setName(tree.getLabel().toString());
		stmt.setLabels(Collections.singletonList(label));
		stmt.setStatement(visitor.scan(tree.getStatement(), p).get(0));

		return Collections.<AstNode>singletonList(stmt);
	}
}
