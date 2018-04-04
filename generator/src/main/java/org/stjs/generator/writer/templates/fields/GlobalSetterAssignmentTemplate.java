package org.stjs.generator.writer.templates.fields;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ExpressionTree;

public class GlobalSetterAssignmentTemplate<JS> implements WriterContributor<AssignmentTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, AssignmentTree tree, GenerationContext<JS> context) {
		//JS left = visitor.scan(tree.getVariable(), context);
		JS right = visitor.scan(tree.getExpression(), context);

		TreeWrapper<ExpressionTree, JS> leftSide = context.getCurrentWrapper().child(tree.getVariable());
		JS target = SetterAssignmentTemplate.getTarget(visitor, leftSide, context);

		JS eg = context.js().elementGet(
				target,
				SetterAssignmentTemplate.getField(leftSide, context)
		);

		return context.js().assignment(AssignOperator.ASSIGN, eg, right);
	}
}
