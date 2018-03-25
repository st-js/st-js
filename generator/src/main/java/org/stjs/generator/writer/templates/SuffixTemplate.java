package org.stjs.generator.writer.templates;

import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * This template can be used to suffix the name of a method that can be Java keyword.<br>
 * When used without parameters as <tt>{@literal @}Template("suffix")</tt>, this template simply strips the trailing
 * character of the Java method name<br>
 * method_() =&gt; method() or method$() =&gt; method() <br>
 *
 * When used with a single parameters, this template removes the last <tt>param.length</tt> characters
 * {@literal @}Template("suffix(Extended)") methodExtended() =&gt; method(), or {@literal @}Template("suffix(Bar)")
 * fooBar() =&gt; foo()
 *
 * @author acraciun
 */
public class SuffixTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

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
			// no parameter behavior is analogous to PrefixTemplate
			return original.substring(0, original.length() - 1);
		}
		return original.substring(0, original.length() - prefixParams[0].length());
	}
}
