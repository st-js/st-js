package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.Tree;

/**
 * @author acraciun
 */
public class ForLoopWriter<JS> implements WriterContributor<ForLoopTree, JS> {

	private JS initializer(WriterVisitor<JS> visitor, ForLoopTree tree, GenerationContext<JS> p) {
		List<JS> jsNodes = new ArrayList<JS>();
		for (Tree u : tree.getInitializer()) {
			jsNodes.add(visitor.scan(u, p));
		}
		return p.js().asExpressionList(jsNodes);
	}

	private JS condition(WriterVisitor<JS> visitor, ForLoopTree tree, GenerationContext<JS> p) {
		if (tree.getCondition() != null) {
			return visitor.scan(tree.getCondition(), p);
		}
		return null;
	}

	private JS update(WriterVisitor<JS> visitor, ForLoopTree tree, GenerationContext<JS> p) {
		List<JS> jsNodes = new ArrayList<JS>();
		for (ExpressionStatementTree u : tree.getUpdate()) {
			jsNodes.add(visitor.scan(u.getExpression(), p));
		}
		return p.js().asExpressionList(jsNodes);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, ForLoopTree tree, GenerationContext<JS> context) {
		JS init = initializer(visitor, tree, context);
		JS condition = condition(visitor, tree, context);
		JS update = update(visitor, tree, context);
		JS body = visitor.scan(tree.getStatement(), context);

		return context.withPosition(tree, context.js().forLoop(init, condition, update, body));
	}
}
