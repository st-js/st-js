package org.stjs.generator.writer.templates.fields;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MemberSelectTree;

/**
 * write member select access, for fields, types and methods.
 *
 * @author acraciun
 * @param <JS>
 */
public class GlobalGetterMemberSelectTemplate<JS> extends GetterMemberSelectTemplate<JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberSelectTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, true);
	}
}
