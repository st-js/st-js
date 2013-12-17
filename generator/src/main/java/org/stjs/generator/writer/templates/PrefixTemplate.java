package org.stjs.generator.writer.templates;

import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * This template can be used to prefix the name of a method that can be Java keyword.<br>
 * $method() => method() or _method() => method() <br>
 * @author acraciun
 */
public class PrefixTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		AstNode target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		String name = MethodInvocationWriter.buildMethodName(tree);
		name = name.substring(1);
		List<AstNode> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return Collections.<AstNode> singletonList(JavaScriptNodes.functionCall(target, name, arguments));
	}
}
