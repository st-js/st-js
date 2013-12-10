package org.stjs.generator.check.declaration;

import javax.lang.model.element.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MethodTree;

/**
 * this class checks that you don't have synchornized methods, as this feature is not supported in JavaScript
 * @author acraciun
 */
public class MethodSynchronizedCheck implements VisitorContributor<MethodTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, MethodTree tree, GenerationContext context, Void prev) {
		if (tree.getModifiers().getFlags().contains(Modifier.SYNCHRONIZED)) {
			context.addError(tree, "Synchronized methods are not supported by Javascript");
		}

		return null;
	}
}
