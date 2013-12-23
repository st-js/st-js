package org.stjs.generator.writer.statement;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;

/**
 * Try blocks -> as in java
 * 
 * @author acraciun
 */
public class TryWriter<JS> implements WriterContributor<TryTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, TryTree tree, GenerationContext<JS> context) {
		JS tryBlock = visitor.scan(tree.getBlock(), context);
		List<JS> catchClauses = new ArrayList<JS>();
		for (Tree c : tree.getCatches()) {
			catchClauses.add(visitor.scan(c, context));
		}
		JS finallyBlock = null;
		if (tree.getFinallyBlock() != null) {
			finallyBlock = visitor.scan(tree.getFinallyBlock(), context);
		}
		return context.withPosition(tree, context.js().tryStatement(tryBlock, catchClauses, finallyBlock));
	}
}
