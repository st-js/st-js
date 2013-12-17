package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ParenthesizedTree;

public class ParenthesizedWriter<JS> implements WriterContributor<ParenthesizedTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ParenthesizedTree tree, GenerationContext<JS> context) {
		return context.js().paren(visitor.scan(tree.getExpression(), context));
	}
}
