package org.stjs.generator.writer.templates;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * array.$set(index, value) -> array[index] = value, or $set(obj, prop, value) -> obj[prop]=value
 *
 * @author acraciun
 */
public class PutTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {
	private final static int MIN_ARGS_COUNT = 2;

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount < MIN_ARGS_COUNT || argCount > MIN_ARGS_COUNT + 1) {
			throw context.addError(tree, "A 'put' template can only be applied for methods with 2 or 3 parameters");
		}

		JS target = null;
		int arg = 0;
		if (argCount == 2) {
			// array.$get(x) -> array[x]
			target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree>getCurrentWrapper());
		} else {
			// $get(obj, prop) -> obj[prop]
			target = context.js().paren(visitor.scan(tree.getArguments().get(arg++), context));
		}
		JS eg = context.js().elementGet(target, visitor.scan(tree.getArguments().get(arg++), context));
		JS value = visitor.scan(tree.getArguments().get(arg++), context);
		return context.js().assignment(AssignOperator.ASSIGN, eg, value);
	}
}
