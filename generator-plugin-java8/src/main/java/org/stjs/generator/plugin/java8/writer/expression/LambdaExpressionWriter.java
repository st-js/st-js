package org.stjs.generator.plugin.java8.writer.expression;

import java.util.Collections;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.declaration.MethodWriter;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LambdaExpressionTree.BodyKind;

/**
 * generates the code for Lambda expressions.
 * 
 * <pre>
 * (x, y) -&gt; x + y
 * </pre>
 * 
 * gives
 * 
 * <pre>
 * function (x,y) {
 * 	return x + y;
 * }
 * </pre>
 * 
 * 
 * @author acraciun
 * 
 * @param <JS>
 */
public class LambdaExpressionWriter<JS> implements WriterContributor<LambdaExpressionTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, LambdaExpressionTree tree, GenerationContext<JS> context) {
		List<JS> params = MethodWriter.getParams(tree.getParameters(), context);

		JS body = visitor.scan(tree.getBody(), context);

		if (tree.getBodyKind() == BodyKind.EXPRESSION) {
			body = context.js().returnStatement(body);
		}

		if (!(tree.getBody() instanceof BlockTree)) {
			body = context.js().block(Collections.singleton(body));
		}

		return context.js().function(null, params, body);
	}

}
