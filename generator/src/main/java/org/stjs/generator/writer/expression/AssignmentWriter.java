package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ExpressionTree;

public class AssignmentWriter<JS> implements WriterContributor<AssignmentTree, JS> {

	public String buildTemplateName(AssignmentTree tree, GenerationContext<JS> context) {
		TreeWrapper<ExpressionTree, JS> leftSide = context.getCurrentWrapper().child(tree.getVariable());

		String name = leftSide.getFieldTemplate();
		if (name != null) {
			return name;
		}
		return "none";
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, AssignmentTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(AssignmentWriter.class.getSimpleName(), templateName), tree, context);
	}
}
