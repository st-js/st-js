package org.stjs.generator.writer.templates;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * This template can be used to prefix the name of a method that can be Java keyword.<br>
 * When used without parameters as <tt>@Template("prefix")</tt>, this template simply strips the
 * leading character of the Java method name<br>
 * $method() => method() or _method() => method() <br>
 *
 * When used with a single parameters, this template removes the first <tt>param.length</tt> characters
 * from the java method name and makes the first letter of the resulting identifier lowercase<br>
 * @Template("prefix(special)") specialMethod() => method(), or @Template("prefix(foo)") fooBar() => bar()
 *
 * @author acraciun
 */
public class PrefixTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree>getCurrentWrapper());
		String name = MethodInvocationWriter.buildMethodName(tree);
		name = transformMethodName(name, context.getCurrentWrapper().getMethodTemplateParameters());
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return context.js().functionCall(context.js().property(target, name), arguments);
	}

	private String transformMethodName(String original, String[] prefixParams) {
		if (prefixParams.length == 0) {
			// backwards compatible behavior
			return original.substring(1);
		}
		String transformed = original.substring(prefixParams[ 0 ].length());
		transformed = Character.toLowerCase(transformed.charAt(0)) + transformed.substring(1);
		return transformed;
	}
}
