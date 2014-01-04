package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ThrowTree;

/**
 * @author acraciun
 */
public class ThrowWriter<JS> implements WriterContributor<ThrowTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ThrowTree tree, GenerationContext<JS> context) {
		JS expr = visitor.scan(tree.getExpression(), context);
		return context.withPosition(tree, context.js().throwStatement(expr));
	}
}
