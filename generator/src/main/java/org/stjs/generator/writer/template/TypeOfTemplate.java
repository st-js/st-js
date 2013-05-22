package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

public class TypeOfTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		// add parentheses around
		currentHandler.getPrinter().print("(typeof ");
		n.getArgs().get(0).accept(currentHandler, context);
		currentHandler.getPrinter().print(")");
		return true;
	}

}
