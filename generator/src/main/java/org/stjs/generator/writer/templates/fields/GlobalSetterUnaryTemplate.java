package org.stjs.generator.writer.templates.fields;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.UnaryTree;

/**
 * Java unary operator
 * @author acraciun
 */
public class GlobalSetterUnaryTemplate<JS> extends SetterUnaryTemplate<JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, true);
	}
}
