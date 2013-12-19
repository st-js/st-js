package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CompoundAssignmentTree;

public class CompoundAssignmentWriter<JS> implements WriterContributor<CompoundAssignmentTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getVariable(), context);
		JS right = visitor.scan(tree.getExpression(), context);
		AssignOperator op = AssignOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		return context.js().assignment(op, left, right);
	}
}
