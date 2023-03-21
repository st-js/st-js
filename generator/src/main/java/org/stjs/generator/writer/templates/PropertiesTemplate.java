package org.stjs.generator.writer.templates;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $properties(obj) -> obj
 * 
 * @author acraciun
 */
public class PropertiesTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw context.addError(tree, "A 'properties' template can only be applied for methods with 1 parameter");
		}

		return context.js().paren(visitor.scan(tree.getArguments().get(0), context));
	}
}
