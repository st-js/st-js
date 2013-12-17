package org.stjs.generator.writer.templates;

import org.mozilla.javascript.Token;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
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
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'delete' template can only be applied for methods with 1 parameter");
		}
		int arg = 0;
		JS target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		JS prop = context.js().elementGet(target, visitor.scan(tree.getArguments().get(arg++), context));
		return context.js().unary(Token.DELPROP, prop);
	}
}
