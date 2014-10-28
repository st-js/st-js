package org.stjs.generator.writer.templates.fields;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CompoundAssignmentTree;

/**
 * Java unary operator
 * @author acraciun
 */
public class GlobalSetterCompoundAssignmentTemplate<JS> extends SetterCompoundAssignmentTemplate<JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, true);
	}
}
