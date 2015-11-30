package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.SynchronizedTree;

/**
 * Synchronized blocks are not allowed
 * 
 * @author acraciun
 */
public class SynchronizedWriter<JS> implements WriterContributor<SynchronizedTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, SynchronizedTree tree, GenerationContext<JS> context) {
		if (context.getConfiguration().isSynchronizedAllowed()) {
			JS js = visitor.visitBlock(tree.getBlock(), context);
			return context.withPosition(tree, js);
		} else {
			context.addError(tree, "Synchronized blocks are not allowed");
			return null;
		}

	}
}
