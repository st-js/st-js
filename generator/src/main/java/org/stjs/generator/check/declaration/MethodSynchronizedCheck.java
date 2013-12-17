package org.stjs.generator.check.declaration;

import javax.lang.model.element.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.MethodTree;

/**
 * this class checks that you don't have synchornized methods, as this feature is not supported in JavaScript
 * @author acraciun
 */
public class MethodSynchronizedCheck implements CheckContributor<MethodTree> {

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		if (tree.getModifiers().getFlags().contains(Modifier.SYNCHRONIZED)) {
			context.addError(tree, "Synchronized methods are not supported by Javascript");
		}

		return null;
	}
}
