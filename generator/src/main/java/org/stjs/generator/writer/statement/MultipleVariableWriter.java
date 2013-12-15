package org.stjs.generator.writer.statement;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.JavaScriptNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;

import com.sun.source.tree.VariableTree;

/**
 * this is a special writer called from a block to handle the case of multiple variables declarated on the same line.
 * This is needed because the Javac AST parser has an separate node per variable
 * 
 * @author acraciun
 * 
 */
public class MultipleVariableWriter {
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, List<VariableTree> trees,
			GenerationContext context) {
		VariableDeclaration stmt = new VariableDeclaration();

		// if it's the init part of a for, mark it as expression, not statement
		// if (isLoopInitializer(context)) {
		// stmt.setIsStatement(false);
		// } else {
		stmt.setIsStatement(true);
		// }

		for (VariableTree tree : trees) {
			VariableInitializer var = new VariableInitializer();
			var.setTarget(JavaScriptNodes.name(tree.getName().toString()));
			if (tree.getInitializer() != null) {
				var.setInitializer(visitor.scan(tree.getInitializer(), context).get(0));
			}

			stmt.addVariable(var);
		}
		return Collections.<AstNode>singletonList(context.withPosition(trees.get(0), stmt));
	}

}
