package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $map(n1, v1, n2, v2) -> {n1: v1, n2: v2}
 * @author acraciun
 */
public class MapTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {

		List<AstNode> names = new ArrayList<AstNode>();
		List<AstNode> values = new ArrayList<AstNode>();
		for (int i = 0; i < tree.getArguments().size(); i += 2) {
			names.add(visitor.scan(tree.getArguments().get(i), context).get(0));
			values.add(visitor.scan(tree.getArguments().get(i + 1), context).get(0));
		}
		return Collections.<AstNode> singletonList(JavaScriptNodes.object(names, values));
	}
}
