package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.EmptyStatementTree;

public class EmptyStatementWriter<JS> implements WriterContributor<EmptyStatementTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, EmptyStatementTree tree, GenerationContext<JS> context) {
		return context.js().emptyStatement();
	}
}
