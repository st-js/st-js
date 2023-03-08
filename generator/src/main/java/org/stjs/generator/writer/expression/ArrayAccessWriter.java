package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ArrayAccessTree;

/**
 * array access to used only in very specific case, otherwise the arrays are forbidden
 * 
 * @author acraciun
 * 
 * @param <JS>
 */
public class ArrayAccessWriter<JS> implements WriterContributor<ArrayAccessTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, ArrayAccessTree tree, GenerationContext<JS> context) {
		JS target = visitor.scan(tree.getExpression(), context);
		JS elem = visitor.scan(tree.getIndex(), context);
		return context.js().elementGet(target, elem);
	}
}
