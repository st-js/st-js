package org.stjs.generator.writer.templates.fields;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
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

		List<JS> arguments = new ArrayList<JS>();
		arguments.add(target);
		arguments.add(SetterAssignmentTemplate.getField(leftSide, context));
		arguments.add(right);

		return context.js().functionCall(context.js().property(context.js().name("stjs"), "setField"), arguments);
	}
}
