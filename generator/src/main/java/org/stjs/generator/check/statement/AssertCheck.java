package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.AssertTree;

/**
 * Java asserts don't have an equivalent - at language level in JavaScript
 * @author acraciun
 */
public class AssertCheck implements VisitorContributor<AssertTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, AssertTree tree, GenerationContext context, Void prev) {

		context.addError(tree, "Assert statement is not supported by JavaScript.");
		return null;
	}

}
