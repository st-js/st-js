package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ContinueTree;

public class ContinueWriter<JS> implements WriterContributor<ContinueTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ContinueTree tree, GenerationContext<JS> context) {
		JS label = null;
		if (tree.getLabel() != null) {
			label = context.js().name(tree.getLabel().toString());
		}

		return context.withPosition(tree, context.js().continueStatement(label));
	}
}
