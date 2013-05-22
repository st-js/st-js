package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

public class InvokeTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		TemplateUtils.printScope(currentHandler, n, context, false);
		// skip methodname
		currentHandler.printArguments(n.getArgs(), context);
		return true;
	}

}
