package org.stjs.generator.writer.templates;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $typeOf(arg) -> (typeof arg)
 * 
 * @author acraciun
 */
public class TypeOfTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw context.addError(tree, "A 'typeof' template can only be applied for methods with 1 parameter");
		}
		JS prop = visitor.scan(tree.getArguments().get(0), context);
		return context.js().paren(context.js().unary(UnaryOperator.TYPEOF, prop));
	}
}
