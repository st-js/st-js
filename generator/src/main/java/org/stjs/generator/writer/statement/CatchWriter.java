package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CatchTree;

/**
 * @author acraciun
 */
public class CatchWriter<JS> implements WriterContributor<CatchTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, CatchTree tree, GenerationContext<JS> context) {
		JS condition = context.js().name(tree.getParameter().getName());
		JS body = visitor.scan(tree.getBlock(), context);
		return context.withPosition(tree, context.js().catchClause(condition, body));
	}
}
