package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.UnaryTree;

/**
 * Java unary operator
 * 
 * @author acraciun
 */
public class UnaryWriter<JS> implements WriterContributor<UnaryTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context) {
		JS operand = visitor.scan(tree.getExpression(), context);
		UnaryOperator op = UnaryOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		return context.js().unary(op, operand);
	}
}
