package org.stjs.generator.writer.templates.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ExpressionTree;

public class SetterCompoundAssignmentTemplate<JS> implements WriterContributor<CompoundAssignmentTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, false);
	}

	@SuppressWarnings("PMD.CompareObjectsWithEquals")
	protected JS doVisit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context, boolean global) {
		AssignOperator op = AssignOperator.valueOf(tree.getKind());
		assert op != null : "Unknown operator:" + tree.getKind();

		JS left = visitor.scan(tree.getVariable(), context);
		JS right = visitor.scan(tree.getExpression(), context);
		JS modifiedRight = DefaultCompoundAssignmentTemplate.rightSide(left, right, tree, context);
		JS value;
		if (modifiedRight == right) {
			value = context.js().binary(op.getBinaryOperator(), Arrays.asList(left, context.js().paren(right)));
		} else {
			value = modifiedRight;
		}

		TreeWrapper<ExpressionTree, JS> leftSide = context.getCurrentWrapper().child(tree.getVariable());
		JS target = SetterAssignmentTemplate.getTarget(visitor, leftSide, context);

		if (global) {
			JS eg = context.js().elementGet(target, SetterAssignmentTemplate.getField(leftSide, context));
			return context.js().assignment(AssignOperator.ASSIGN, eg, value);
		}

		List<JS> arguments = new ArrayList<>();
		arguments.add(SetterAssignmentTemplate.getField(leftSide, context));
		arguments.add(value);
		return context.js().functionCall(context.js().property(target, "set"), arguments);
	}
}
