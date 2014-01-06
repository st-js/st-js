package org.stjs.generator.writer.templates;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * map.$delete(key) -> delete map[key]
 * 
 * @author acraciun
 */
public class DeleteTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw context.addError(tree, "A 'delete' template can only be applied for methods with 1 parameter");
		}
		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree>getCurrentWrapper());
		JS prop = context.js().elementGet(target, visitor.scan(tree.getArguments().get(0), context));
		return context.js().unary(UnaryOperator.DELETE_PROPERTY, prop);
	}
}
