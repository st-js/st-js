package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ConditionalExpressionTree;

/**
 * conditional expr - as in java
 * 
 * @author acraciun
 * 
 * @param <JS>
 */
public class ConditionalWriter<JS> implements WriterContributor<ConditionalExpressionTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ConditionalExpressionTree tree, GenerationContext<JS> context) {
		JS test = visitor.scan(tree.getCondition(), context);
		JS trueExpr = visitor.scan(tree.getTrueExpression(), context);
		JS falseExpr = visitor.scan(tree.getFalseExpression(), context);
		return context.js().conditionalExpression(test, trueExpr, falseExpr);
	}
}
