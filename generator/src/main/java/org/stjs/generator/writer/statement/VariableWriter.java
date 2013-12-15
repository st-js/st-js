package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.declaration.FieldWriter;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public class VariableWriter implements VisitorContributor<VariableTree, List<AstNode>, GenerationContext> {
	private final FieldWriter fieldWriter = new FieldWriter();

	private boolean isLoopInitializer(GenerationContext context) {
		Tree parent = context.getCurrentPath().getParentPath().getLeaf();
		return parent instanceof ForLoopTree || parent instanceof EnhancedForLoopTree;
	}

	private List<AstNode> fieldDeclaration(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, VariableTree tree,
			GenerationContext context, List<AstNode> prev) {
		if (context.getCurrentPath().getParentPath().getLeaf() instanceof ClassTree) {
			return fieldWriter.visit(visitor, tree, context, prev);
		}
		return null;
	}

	@Override
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, VariableTree tree,
			GenerationContext context, List<AstNode> prev) {
		List<AstNode> js = null;
		js = fieldDeclaration(visitor, tree, context, prev);
		if (js != null) {
			return js;
		}
		VariableDeclaration stmt = new VariableDeclaration();

		// if it's the init part of a for, mark it as expression, not statement
		if (isLoopInitializer(context)) {
			stmt.setIsStatement(false);
		} else {
			stmt.setIsStatement(true);
		}

		VariableInitializer var = new VariableInitializer();
		var.setTarget(JavaScriptNodes.name(tree.getName().toString()));
		if (tree.getInitializer() != null) {
			var.setInitializer(visitor.scan(tree.getInitializer(), context).get(0));
		}

		stmt.addVariable(var);

		return Collections.<AstNode>singletonList(context.withPosition(tree, stmt));
	}

}
