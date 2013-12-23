package org.stjs.generator.plugin.java8.writer.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MemberReferenceTree;

/**
 * this class is for reference to a member like:<br>
 * 
 * Static method reference: String::valueOf
 * 
 * Non-static method reference: Object::toString
 * 
 * Capturing method reference: x::toString
 * 
 * Constructor reference: ArrayList::new
 * 
 * @author acraciun
 * 
 */
public class MemberReferenceWriter<JS> implements WriterContributor<MemberReferenceTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MemberReferenceTree tree, GenerationContext<JS> p) {
		// TODO Auto-generated method stub
		return null;
	}

}
