package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;

/**
 * $array() -> []
 * 
 * @author acraciun
 */
public class ArrayTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		List<JS> values = new ArrayList<JS>();
		for (ExpressionTree arg : tree.getArguments()) {
			values.add(visitor.scan(arg, context));
		}

		return context.js().array(values);
	}
}
