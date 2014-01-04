package org.stjs.generator.writer.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.AssertTree;

/**
 * asserts - not supported
 * 
 * @author acraciun
 */
public class AssertWriter<JS> implements WriterContributor<AssertTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, AssertTree tree, GenerationContext<JS> p) {
		p.addError(tree, "Java assert are not allowed");
		return null;
	}
}
