package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.DoWhileLoopTree;

public class DoWhileLoopWriter<JS> implements WriterContributor<DoWhileLoopTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, DoWhileLoopTree tree, GenerationContext<JS> context) {
		JS condition = visitor.scan(TreeUtils.skipParens(tree.getCondition()), context);
		JS body = visitor.scan(tree.getStatement(), context);
		return context.withPosition(tree, context.js().doLoop(condition, body));
	}
}
