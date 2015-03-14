package org.stjs.generator.plugin.java8.check.expression;

import javax.lang.model.element.Name;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;

/**
 * This check class verifies that all final variables used in loops are declared at the method level, as the block scope
 * does not exists in JavaScript. <br>
 * It prevents cases likes this one:<br>
 * 
 * <pre>
 *     	for(var x = 0; x &lt; 3; ++x) { 
 *     		setTimeout(function(){console.info("x=" + x);}, 0); 
 *     	}
 * </pre>
 * 
 * will display: "x=3" - 3 times!
 * 
 * @author acraciun
 */
public class LambdaAccessFinalInLoopCheck implements CheckContributor<VariableTree> {
	private static boolean isLoop(TreePath path) {
		Tree tree = path.getLeaf();
		return tree instanceof ForLoopTree || tree instanceof EnhancedForLoopTree || tree instanceof WhileLoopTree;
	}

	private static boolean isMethodOrClassDeclaration(TreePath path) {
		Tree tree = path.getLeaf();
		return tree instanceof MethodTree || tree instanceof ClassTree;
	}

	private boolean isVariable(GenerationContext<Void> context) {
		if (context.getCurrentPath().getParentPath().getLeaf() instanceof ClassTree) {
			return false;
		}
		if (context.getCurrentPath().getParentPath().getLeaf() instanceof MethodTree) {
			return false;
		}
		if (context.getCurrentPath().getParentPath().getLeaf() instanceof LambdaExpressionTree) {
			return false;
		}
		return true;
	}

	private void checkVarInLambda(Name outsideVarName, LambdaExpressionTree lambda, GenerationContext<Void> context) {
		lambda.accept(new TreeScanner<Void, Void>() {
			@Override
			public Void visitIdentifier(IdentifierTree ident, Void arg1) {
				if (ident.getName().equals(outsideVarName)) {
					context.addError(
							ident,
							"To prevent unexpected behaviour in Javascript, final (or effectively final) variables must be declared at method level and not inside loops. Variable to be moved: "
									+ ident.getName());
				}
				return super.visitIdentifier(ident, arg1);
			}

		}, null);

	}

	private void checkUsageInLambdas(VariableTree tree, GenerationContext<Void> context) {
		// this is the block containing the var definition
		TreePath blockPath = context.getCurrentPath().getParentPath();
		blockPath.getLeaf().accept(new TreeScanner<Void, Void>() {
			@Override
			public Void visitLambdaExpression(LambdaExpressionTree lambda, Void arg1) {
				checkVarInLambda(tree.getName(), lambda, context);
				return super.visitLambdaExpression(lambda, arg1);
			}

		}, null);
	}

	@Override
	public Void visit(CheckVisitor visitor, VariableTree tree, GenerationContext<Void> context) {
		if (!isVariable(context))
			return null;
		for (TreePath p = context.getCurrentPath(); p != null; p = p.getParentPath()) {
			if (isLoop(p)) {
				checkUsageInLambdas(tree, context);
				break;

			}
			if (isMethodOrClassDeclaration(p)) {
				break;
			}
		}
		return null;
	}

}
