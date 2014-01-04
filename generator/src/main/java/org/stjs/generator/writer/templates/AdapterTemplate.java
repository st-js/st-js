package org.stjs.generator.writer.templates;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * method(x, arg1, arg2) -> x.method(arg1, arg2)
 * 
 * @author acraciun
 */
public class AdapterTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount < 1) {
			throw context.addError(tree, "An 'adapter' template can only be applied for methods with at least 1 parameter");
		}
		String name = MethodInvocationWriter.buildMethodName(tree);
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		JS target = context.js().paren(arguments.get(0));

		return context.js().functionCall(context.js().property(target, name), arguments.subList(1, arguments.size()));
	}
}
