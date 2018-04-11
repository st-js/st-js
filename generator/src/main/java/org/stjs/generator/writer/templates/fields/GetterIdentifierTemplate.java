package org.stjs.generator.writer.templates.fields;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;

public class GetterIdentifierTemplate<JS> implements WriterContributor<IdentifierTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, IdentifierTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, false);
	}

	protected JS doVisit(WriterVisitor<JS> visitor, IdentifierTree tree, GenerationContext<JS> context, boolean global) {
		JS target = MemberWriters.buildTarget(context.getCurrentWrapper());

		// THIS is called only for fields
		String fieldName = tree.getName().toString();
		JS name = context.js().string(fieldName);

		List<JS> arguments = new ArrayList<JS>();
		arguments.add(name);

		if (global) {
			return context.js().elementGet(target, name);
		}
		return context.js().functionCall(context.js().property(target, "get"), arguments);
	}
}
