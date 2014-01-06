package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.MethodTree;

/**
 * this class verifies that you use @Template only inside a bridge
 * 
 * @author acraciun
 */
public class MethodDeclarationTemplateCheck implements CheckContributor<MethodTree> {

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		if (context.getCurrentWrapper().getMethodTemplate() != null) {
			context.addError(tree, "You can only use @Template annotation in bridge classes");
			return null;
		}

		return null;
	}
}
