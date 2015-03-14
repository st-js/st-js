package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.javascript.annotation.ServerSide;

import com.sun.source.tree.MethodInvocationTree;

/**
 * this check verifies that you don't call a method from the outer type as in javaScript this scope is not accessible.
 * @author acraciun
 */
public class MethodInvocationServerSideCheck implements CheckContributor<MethodInvocationTree> {

	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		Element methodElement = TreeUtils.elementFromUse(tree);

		if (methodElement.getAnnotation(ServerSide.class) != null) {
			context.addError(tree, "You cannot access methods annotated with @ServerSide in a client code");
		}

		return null;
	}
}
