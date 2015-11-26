package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.util.TreePath;

/**
 * this checks that no java array with 3 dimens or more are used.
 */
public class NewArrayMultipleDimensForbiddenCheck implements CheckContributor<NewArrayTree> {

	@Override
	public Void visit(CheckVisitor visitor, NewArrayTree tree, GenerationContext<Void> context) {
		if (isAnnotationParam(context.getCurrentPath())) {
			return null;
		}
		if (tree.getInitializers() == null && tree.getDimensions().size() > 2) {
			context.addError(tree, "You cannot use Java arrays without initializers that are more than 2 dimensions.");
		}
		return null;
	}

	private boolean isAnnotationParam(TreePath currentPath) {
		return currentPath.getParentPath().getParentPath().getLeaf() instanceof AnnotationTree;
	}

}
