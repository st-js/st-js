package org.stjs.generator.writer.templates.fields;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;

public class GetterIdentifierTemplate<JS> implements WriterContributor<IdentifierTree, JS> {
	public static <JS> JS getField(TreeWrapper<? extends Tree, JS> expr, GenerationContext<JS> context) {
		if (expr.getTree() instanceof IdentifierTree) {
			return context.js().string(((IdentifierTree) expr.getTree()).getName().toString());
		}
		if (expr.getTree() instanceof MemberSelectTree) {
			return context.js().string(((MemberSelectTree) expr.getTree()).getIdentifier().toString());
		}
		// XXX: what else we can have here !?
		context.addError(expr.getTree(), "Unexpected node type:" + expr.getTree().getClass());
		return null;
	}

	public static <JS> JS getTarget(WriterVisitor<JS> visitor, TreeWrapper<? extends Tree, JS> expr, GenerationContext<JS> context) {
		if (expr.getTree() instanceof IdentifierTree) {
			return MemberWriters.buildTarget(expr);
		}
		if (expr.getTree() instanceof MemberSelectTree) {
			return visitor.scan(((MemberSelectTree) expr.getTree()).getExpression(), context);
		}
		// XXX: what else we can have here !?
		context.addError(expr.getTree(), "Unexpected node type:" + expr.getTree().getClass());
		return null;
	}

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
