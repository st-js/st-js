package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.LabeledStatementTree;

public class LabeledStatementWriter<JS> implements WriterContributor<LabeledStatementTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, LabeledStatementTree tree, GenerationContext<JS> context) {
		JS label = context.js().label(tree.getLabel());
		JS statement = visitor.scan(tree.getStatement(), context);

		return context.withPosition(tree, context.js().labeledStatement(label, statement));
	}
}
