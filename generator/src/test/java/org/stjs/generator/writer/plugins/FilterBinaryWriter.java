package org.stjs.generator.writer.plugins;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.WriterFilter;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BinaryTree;

public class FilterBinaryWriter<JS> implements WriterFilter<BinaryTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, BinaryTree tree, GenerationContext<JS> p,
			VisitorContributor<BinaryTree, JS, GenerationContext<JS>, WriterVisitor<JS>> chain) {
		JS result = chain.visit(visitor, tree, p);
		if (result == null) {
			return null;
		}
		// add parentheses to each binary expression
		return p.js().paren(result);
	}

}
