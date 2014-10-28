package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ExpressionTree;

public class CompoundAssignmentWriter<JS> implements WriterContributor<CompoundAssignmentTree, JS> {

	public String buildTemplateName(CompoundAssignmentTree tree, GenerationContext<JS> context) {
		TreeWrapper<ExpressionTree, JS> leftSide = context.getCurrentWrapper().child(tree.getVariable());

		String name = leftSide.getFieldTemplate();
		if (name != null) {
			return name;
		}
		return "none";
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		String templateName = buildTemplateName(tree, context);

		return visitor.forward(DiscriminatorKey.of(CompoundAssignmentWriter.class.getSimpleName(), templateName), tree, context);
	}

}
