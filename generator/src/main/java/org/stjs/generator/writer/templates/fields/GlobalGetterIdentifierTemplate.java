package org.stjs.generator.writer.templates.fields;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;

public class GlobalGetterIdentifierTemplate<JS> extends GetterIdentifierTemplate<JS> {
	@Override
	public JS visit(WriterVisitor<JS> visitor, IdentifierTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, true);
	}
}
