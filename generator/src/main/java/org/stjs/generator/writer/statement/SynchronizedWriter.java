package org.stjs.generator.writer.statement;

import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.SynchronizedTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Synchronized blocks are not allowed
 * 
 * @author acraciun
 */
public class SynchronizedWriter<JS> implements WriterContributor<SynchronizedTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, SynchronizedTree tree, GenerationContext<JS> context) {
		if (context.getConfiguration().isSynchronizedAllowed()) {
			List<JS> jsStatements = new ArrayList();

			for (StatementTree statementTree : tree.getBlock().getStatements()) {
				jsStatements.add(visitor.scan(statementTree, context));
			}

			return context.withPosition(tree, context.js().statements(jsStatements));
		} else {
			context.addError(tree, "Synchronized blocks are not allowed");
			return null;
		}

	}
}
