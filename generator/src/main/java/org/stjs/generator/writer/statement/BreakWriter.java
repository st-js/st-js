package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BreakTree;

/**
 * @author acraciun
 */
public class BreakWriter<JS> implements WriterContributor<BreakTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, BreakTree tree, GenerationContext<JS> context) {
		JS label = null;
		if (tree.getLabel() != null) {
			label = context.js().name(tree.getLabel().toString());
		}

		return context.withPosition(tree, context.js().breakStatement(label));
	}
}
