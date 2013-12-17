package org.stjs.generator.writer.statement;

import org.mozilla.javascript.ast.WhileLoop;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.WhileLoopTree;

/**
 * while loop - as in Java
 * @author acraciun
 */
public class WhileLoopWriter<JS> implements WriterContributor<WhileLoopTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, WhileLoopTree tree, GenerationContext<JS> context) {
		WhileLoop stmt = new WhileLoop();
		JS condition = visitor.scan(tree.getCondition(), context);
		JS body = visitor.scan(tree.getStatement(), context);
		return context.withPosition(tree, context.js().whileLoop(condition, body));
	}
}
