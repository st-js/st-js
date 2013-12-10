package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.SynchronizedTree;

/**
 * ths synchronized blocks are illegal, as there is no threading model in JavaScript
 * @author acraciun
 */
public class SynchronizedCheck implements VisitorContributor<SynchronizedTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, SynchronizedTree tree, GenerationContext context, Void prev) {
		context.addError(tree, "Synchronized blocks are not supported by Javascript");
		return null;
	}

}
