package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.SwitchCase;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.CaseTree;
import com.sun.source.tree.Tree;

public class CaseWriter implements VisitorContributor<CaseTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, CaseTree tree, GenerationContext context,
			List<AstNode> prev) {
		// TODO: check qualified enums:
		// if (selectorType instanceof ClassWrapper && ((ClassWrapper) selectorType).getClazz().isEnum()) {
		// printer.print(names.getTypeName(selectorType));
		// printer.print(".");

		SwitchCase stmt = new SwitchCase();
		if (tree.getExpression() != null) {
			stmt.setExpression(visitor.scan(tree.getExpression(), context).get(0));
		}
		for (Tree c : tree.getStatements()) {
			stmt.addStatement(visitor.scan(c, context).get(0));
		}

		return Collections.<AstNode> singletonList(context.withPosition(tree, stmt));
	}
}
