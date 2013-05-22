package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import java.util.Arrays;
import java.util.Collections;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

public class AssertTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		TemplateUtils.printScope(currentHandler, n, context, false);
		currentHandler.getPrinter().print(n.getName());
		String location = "\"" + context.getInputFile().getName() + ":" + n.getBeginLine() + "\"";
		String params = "\"" + n.toString().replace("\"", "\\\"") + "\"";
		currentHandler.printArguments(Arrays.asList(location, params), n.getArgs(), Collections.<String> emptyList(),
				context);
		return true;
	}

}
