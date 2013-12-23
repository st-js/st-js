package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.AssertTree;

/**
 * Java asserts don't have an equivalent - at language level in JavaScript
 * 
 * @author acraciun
 */
public class AssertCheck implements CheckContributor<AssertTree> {

	@Override
	public Void visit(CheckVisitor visitor, AssertTree tree, GenerationContext<Void> context) {
		context.addError(tree, "Assert statement is not supported by JavaScript.");
		return null;
	}

}
