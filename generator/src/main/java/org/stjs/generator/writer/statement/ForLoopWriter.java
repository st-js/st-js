package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * @author acraciun
 */
public class ForLoopWriter<JS> implements WriterContributor<ForLoopTree, JS> {
	private final MultipleVariableWriter<JS> initializerWriter = new MultipleVariableWriter<JS>();

	/**
	 * if have basically 3 cases: <br>
	 * 1) empty initializers for(;i < 10; ++i) <br>
	 * 2) variables: for(int i =0, j =2; ..) <br>
	 * 3) expressions: for(i =0; j =2; ...)
	 */
	@SuppressWarnings("unchecked")
	private JS initializer(WriterVisitor<JS> visitor, ForLoopTree tree, GenerationContext<JS> p) {
		if (tree.getInitializer().isEmpty()) {
			// 1) empty
			return p.js().emptyExpression();
		}
		if (tree.getInitializer().get(0) instanceof VariableTree) {
			// 2) variables
			return initializerWriter.visit(visitor, (List<VariableTree>) tree.getInitializer(), p, false);
		}

		// 3) expressionns
		List<JS> nodes = new ArrayList<JS>();
		for (Tree u : tree.getInitializer()) {
			if (u instanceof ExpressionStatementTree) {
				nodes.add(visitor.scan(((ExpressionStatementTree) u).getExpression(), p));
			}
		}
		return p.js().asExpressionList(nodes);
	}

	private JS condition(WriterVisitor<JS> visitor, ForLoopTree tree, GenerationContext<JS> p) {
		if (tree.getCondition() != null) {
			return visitor.scan(tree.getCondition(), p);
		}
        // 1) empty
        return p.js().emptyExpression();
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
