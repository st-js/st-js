package org.stjs.generator.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.NewArrayTree;

/**
 * useless as arrays are supposed to be checked before
 * 
 * @author acraciun
 */
public class NewArrayWriter<JS> implements WriterContributor<NewArrayTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, NewArrayTree tree, GenerationContext<JS> context) {
		throw context.addError(tree, "Java arrays are not supported. This is a ST-JS bug.");
	}
}
