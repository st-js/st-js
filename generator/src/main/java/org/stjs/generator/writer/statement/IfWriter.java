package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.IfTree;

/**
 * @author acraciun
 */
public class IfWriter<JS> implements WriterContributor<IfTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, IfTree tree, GenerationContext<JS> context) {
		JS condition = visitor.scan(TreeUtils.skipParens(tree.getCondition()), context);
		JS thenPart = visitor.scan(tree.getThenStatement(), context);
		JS elsePart = null;

		if (tree.getElseStatement() != null) {
			elsePart = visitor.scan(tree.getElseStatement(), context);
		}
		return context.withPosition(tree, context.js().ifStatement(condition, thenPart, elsePart));
	}
}
