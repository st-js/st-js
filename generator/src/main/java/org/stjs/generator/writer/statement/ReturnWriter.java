package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ReturnTree;

/**
 * @author acraciun
 */
public class ReturnWriter<JS> implements WriterContributor<ReturnTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ReturnTree tree, GenerationContext<JS> context) {
		JS expr = null;
		if (tree.getExpression() != null) {
			expr = visitor.scan(tree.getExpression(), context);
		}
		return context.withPosition(tree, context.js().returnStatement(expr));
	}
}
