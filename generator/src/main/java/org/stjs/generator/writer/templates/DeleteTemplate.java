package org.stjs.generator.writer.templates;

import java.util.Collections;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * map.$delete(key) -> delete map[key]
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
		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		AstNode prop = elementGet(target, visitor.scan(tree.getArguments().get(arg++), context).get(0));
		return Collections.<AstNode> singletonList(JavaScriptNodes.unary(Token.DELPROP, prop));
	}
}
