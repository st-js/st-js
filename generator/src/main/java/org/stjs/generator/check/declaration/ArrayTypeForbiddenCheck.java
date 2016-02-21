package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.ArrayTypeTree;

/**
 * this class checks that you don't use java arrays in the code (the only exception is the main method - but maybe this
 * only should also be forbidden). You should use {@link org.stjs.javascript.Array instead}.
 * 
 * @author acraciun
 */
public class ArrayTypeForbiddenCheck implements CheckContributor<ArrayTypeTree> {

	@Override
	public Void visit(CheckVisitor visitor, ArrayTypeTree tree, GenerationContext<Void> context) {
		return null;
	}

}
