package org.stjs.generator.writer.template;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * $array() -> []
 * 
 * @author acraciun
 * 
 */
public class ArrayTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		currentHandler.getPrinter().print("[");
		if (n.getArgs() != null) {
			boolean first = true;
			for (Expression arg : n.getArgs()) {
				if (!first) {
					currentHandler.getPrinter().print(", ");
				}
				arg.accept(currentHandler, context);
				first = false;
			}
		}
		currentHandler.getPrinter().print("]");
		return true;
	}

}
