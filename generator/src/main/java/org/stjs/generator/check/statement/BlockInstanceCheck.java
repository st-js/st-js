package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.util.TreePath;

/**
 * this class checks that you cannot have instance initializer blocks (other than the one for special double brace
 * construction) as you can have only a constructor in a JavaScript class-like construction.
 * @author acraciun
 */
public class BlockInstanceCheck implements VisitorContributor<BlockTree, Void, GenerationContext> {

	public static boolean isInitializationBlock(TreePath path) {
		return path.getParentPath().getLeaf() instanceof ClassTree;
	}

	private boolean isObjectinitializerBlock(TreePath path) {
		//TODO some additional checks may be needed to make sure there are not other construction this condition fits
		return isInitializationBlock(path) && path.getParentPath().getParentPath().getLeaf() instanceof NewClassTree;
	}

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, BlockTree tree, GenerationContext context, Void prev) {
		if (!isInitializationBlock(context.getCurrentPath())) {
			return null;
		}
		if (isObjectinitializerBlock(context.getCurrentPath())) {
			return null;
		}
		if (!tree.isStatic()) {
			context.addError(tree, "Initializing blocks are not supported by Javascript. Check also wrongly placed semicolumns.");
		}
		return null;
	}

}
