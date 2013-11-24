package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.JavaScriptNodes;

import com.sun.source.tree.VariableTree;

public class VariableWriter implements VisitorContributor<VariableTree, List<AstNode>, GenerationContext> {

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, VariableTree tree, GenerationContext p,
			List<AstNode> prev) {
		VariableDeclaration stmt = new VariableDeclaration();
		stmt.setIsStatement(true);

		VariableInitializer var = new VariableInitializer();
		var.setTarget(JavaScriptNodes.name(tree.getName().toString()));
		var.setInitializer(visitor.scan(tree.getInitializer(), p).get(0));

		stmt.addVariable(var);

		// TODO add here all the variables on the same line
		return Collections.<AstNode>singletonList(stmt);
	}
}
