package org.stjs.generator.writer.templates.fields;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.AssignmentTree;

public class DefaultAssignmentTemplate<JS> implements WriterContributor<AssignmentTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, AssignmentTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getVariable(), context);
		JS right = visitor.scan(tree.getExpression(), context);

		return context.js().assignment(AssignOperator.ASSIGN, left, right);
	}
}
