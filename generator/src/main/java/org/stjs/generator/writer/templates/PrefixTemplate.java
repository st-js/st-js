package org.stjs.generator.writer.templates;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * This template can be used to prefix the name of a method that can be Java keyword.<br>
 * $method() => method() or _method() => method() <br>
 * 
 * @author acraciun
 */
public class PrefixTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree>getCurrentWrapper());
		String name = MethodInvocationWriter.buildMethodName(tree);
		name = name.substring(1);
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return context.js().functionCall(context.js().property(target, name), arguments);
	}
}
