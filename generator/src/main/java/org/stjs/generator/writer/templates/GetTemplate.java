package org.stjs.generator.writer.templates;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * array.$get(x) -> array[x], or $get(obj, prop) -> obj[prop]
 * 
 * @author acraciun
 */
public class GetTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount != 1 && argCount != 2) {
			context.addError(tree, "A 'get' template can only be applied for methods with 1 or 2 parameters");
			return null;
		}
		JS target = null;
		int arg = 0;
		if (argCount == 1) {
			// array.$get(x) -> array[x]
			target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree>getCurrentWrapper());
		} else {
			// $get(obj, prop) -> obj[prop]
			target = context.js().paren(visitor.scan(tree.getArguments().get(arg++), context));
		}

		return context.js().elementGet(target, visitor.scan(tree.getArguments().get(arg), context));
	}
}
