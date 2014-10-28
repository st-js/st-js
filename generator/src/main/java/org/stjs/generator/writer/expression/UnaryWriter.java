package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.UnaryTree;

/**
 * Java unary operator
 * @author acraciun
 */
public class UnaryWriter<JS> implements WriterContributor<UnaryTree, JS> {

	public String buildTemplateName(UnaryTree tree, GenerationContext<JS> context) {
		TreeWrapper<ExpressionTree, JS> leftSide = context.getCurrentWrapper().child(tree.getExpression());

		String name = leftSide.getFieldTemplate();
		if (name != null) {
			return name;
		}
		return "none";
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(UnaryWriter.class.getSimpleName(), templateName), tree, context);
	}
}
