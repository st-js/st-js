package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionStatementTree;

public class ExpressionStatementWriter<JS> implements WriterContributor<ExpressionStatementTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ExpressionStatementTree tree, GenerationContext<JS> context) {
		JS expression = visitor.scan(tree.getExpression(), context);
		return context.withPosition(tree, context.js().expressionStatement(expression));
	}
}
