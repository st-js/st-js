package org.stjs.generator.writer.templates;

import java.util.Collections;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * array.$set(index, value) -> array[index] = value, or $set(obj, prop, value) -> obj[prop]=value
 * @author acraciun
 */
public class PutTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {
	private final static int MIN_ARGS_COUNT = 2;

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount < MIN_ARGS_COUNT && argCount > MIN_ARGS_COUNT + 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'put' template can only be applied for methods with 2 or 3 parameters");
		}

		AstNode target = null;
		int arg = 0;
		if (argCount == 2) {
			// array.$get(x) -> array[x]
			target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		} else {
			//$get(obj, prop) -> obj[prop]
			target = JavaScriptNodes.paren(visitor.scan(tree.getArguments().get(arg++), context).get(0));
		}
		ElementGet eg = elementGet(target, visitor.scan(tree.getArguments().get(arg++), context).get(0));
		AstNode value = visitor.scan(tree.getArguments().get(arg++), context).get(0);
		return Collections.<AstNode> singletonList(JavaScriptNodes.assignment(eg, value));
	}
}
