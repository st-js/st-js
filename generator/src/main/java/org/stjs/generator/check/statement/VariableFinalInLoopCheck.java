package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.util.TreePath;

/**
 * This check class verifies that all final variables are declared at the method level, as the block scope does not
 * exists in JavaScript.
 * 
 * @author acraciun
 */
public class VariableFinalInLoopCheck implements CheckContributor<VariableTree> {
	private static boolean isLoop(TreePath path) {
		Tree tree = path.getLeaf();
		return tree instanceof ForLoopTree || tree instanceof EnhancedForLoopTree || tree instanceof WhileLoopTree;
	}

	private static boolean isMethodOrClassDeclaration(TreePath path) {
		Tree tree = path.getLeaf();
		return tree instanceof MethodTree || tree instanceof ClassTree;
	}

	@Override
	public Void visit(CheckVisitor visitor, VariableTree tree, GenerationContext<Void> context) {
		if (!JavaNodes.isFinal(tree)) {
			return null;
		}
		for (TreePath p = context.getCurrentPath(); p != null; p = p.getParentPath()) {
			if (isLoop(p)) {
				context.addError(p.getLeaf(),
						"To prevent unexpected behaviour in Javascript, final variables must be declared at method level and not inside loops");
			}
			if (isMethodOrClassDeclaration(p)) {
				break;
			}
		}
		return null;
	}
}
