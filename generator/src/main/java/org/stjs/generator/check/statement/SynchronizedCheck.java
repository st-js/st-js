package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.SynchronizedTree;

/**
 * ths synchronized blocks are illegal, as there is no threading model in JavaScript
 * 
 * @author acraciun
 */
public class SynchronizedCheck implements CheckContributor<SynchronizedTree> {

	@Override
	public Void visit(CheckVisitor visitor, SynchronizedTree tree, GenerationContext<Void> context) {
		context.addError(tree, "Synchronized blocks are not supported by Javascript");
		return null;
	}

}
