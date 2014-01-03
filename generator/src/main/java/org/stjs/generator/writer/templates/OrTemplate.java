package org.stjs.generator.writer.templates;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $or(x, y, z) -> (x || y || z)
 * 
 * @author acraciun
 */
public class OrTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount < 2) {
			throw context.addError(tree, "A 'or' template can only be applied for methods with at least 2 parameters");
		}
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return context.js().paren(context.js().binary(BinaryOperator.CONDITIONAL_OR, arguments));
	}
}
