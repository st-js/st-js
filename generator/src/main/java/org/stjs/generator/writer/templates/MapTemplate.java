package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.NameValue;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $map(n1, v1, n2, v2) -> {n1: v1, n2: v2}
 * 
 * @author acraciun
 */
public class MapTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		List<NameValue<JS>> props = new ArrayList<NameValue<JS>>();
		for (int i = 0; i < tree.getArguments().size(); i += 2) {
			String name = context.js().toString(visitor.scan(tree.getArguments().get(i), context));
			JS value = visitor.scan(tree.getArguments().get(i + 1), context);
			props.add(NameValue.of(name, value));
		}
		return context.js().object(props);
	}
}
