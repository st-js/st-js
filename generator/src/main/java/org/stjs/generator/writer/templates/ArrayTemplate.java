package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;

/**
 * $array() -> []
 * @author acraciun
 */
public class ArrayTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		List<AstNode> values = new ArrayList<AstNode>();
		for (ExpressionTree arg : tree.getArguments()) {
			values.addAll(visitor.scan(arg, context));
		}

		return Collections.<AstNode> singletonList(JavaScriptNodes.array(values));
	}
}
