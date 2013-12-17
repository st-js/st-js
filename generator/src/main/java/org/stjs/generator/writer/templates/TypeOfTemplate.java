package org.stjs.generator.writer.templates;

import java.util.Collections;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $typeOf(arg) -> (typeof arg)
 * @author acraciun
 */
public class TypeOfTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount != 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'typeof' template can only be applied for methods with 1 parameter");
		}
		AstNode prop = visitor.scan(tree.getArguments().get(0), context).get(0);
		return Collections.<AstNode> singletonList(paren(unary(Token.TYPEOF, prop)));
	}
}
