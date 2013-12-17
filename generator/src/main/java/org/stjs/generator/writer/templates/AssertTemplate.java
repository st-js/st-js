package org.stjs.generator.writer.templates;

import japa.parser.ast.expr.MethodCallExpr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.writer.JavascriptWriterVisitor;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;
import org.stjs.generator.writer.template.TemplateUtils;

import com.sun.source.tree.MethodInvocationTree;

/**
 * This template generate a code that allows you to add into the javascript file the original java code and its position
 * in the source file. It's no longer very useful since the javaScript stacktrace can be translated back to Java
 * stacktrace. assertMethod(params) -> assertMethod("sourceFile:line", "assertMethod(params)", params);
 * 
 * @author acraciun
 */
public class AssertTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext<JS> context) {
		TemplateUtils.printScope(currentHandler, n, context, false);
		currentHandler.getPrinter().print(n.getName());
		String location = "\"" + context.getInputFile().getName() + ":" + n.getBeginLine() + "\"";
		String params = "\"" + n.toString().replace("\"", "\\\"") + "\"";
		currentHandler.printArguments(Arrays.asList(location, params), n.getArgs(), Collections.<String>emptyList(), context);
		return true;
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount < 1) {
			throw new JavascriptFileGenerationException(context.getInputFile(), null,
					"An 'adapter' template can only be applied for methods with at least 1 parameter");
		}
		String name = MethodInvocationWriter.buildMethodName(tree);
		JS target = MethodInvocationWriter.buildTarget(visitor, tree, context);
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		arguments.add(0, context.js().string(context.getInputFile().getName() + ":" + context.getStartLine(tree)));
		arguments.add(1, context.js().string(tree.toString()));

		return context.js().functionCall(context.js().property(target, name), arguments);
	}
}
