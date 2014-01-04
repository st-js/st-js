package org.stjs.generator.check.declaration;

import javax.lang.model.element.ExecutableElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.MethodTree;

/**
 * this check verifies that only one method (or constructor) with a given name has actually a body, all the other should
 * be marked as native (or @Native). More the method having the body must be the more generic than the other overloaded
 * methods, so , when generated in the JavaScript, it knows how to handle all the calls.
 * 
 * @author acraciun
 */
public class MethodDeclarationTemplateCheck implements CheckContributor<MethodTree> {

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		ExecutableElement methodElement = TreeUtils.elementFromDeclaration(tree);
		if (JavaNodes.getMethodTemplate(context.getElements(), methodElement) != null) {
			context.addError(tree, "You can only use @Template annotation in bridge classes");
			return null;
		}

		return null;
	}
}
