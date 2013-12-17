package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $or(x, y, z) -> (x || y || z)
 * @author acraciun
 */
public class OrTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount < 2) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"A 'or' template can only be applied for methods with at least 2 parameters");
		}
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return Collections.<AstNode> singletonList(paren(binary(Token.OR, arguments)));
	}
}
