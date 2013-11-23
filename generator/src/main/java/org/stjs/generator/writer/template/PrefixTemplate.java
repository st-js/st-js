package org.stjs.generator.writer.template;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * This template can be used to prefix the name of a method that can be Java keyword.<br>
 * $method() => method() or _method() => method() <br>
 * 
 * @author acraciun
 */
public class PrefixTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		currentHandler.getPrinter().print(n.getName().substring(1));
		currentHandler.getPrinter().print("(");
		if (n.getArgs() != null) {
			boolean first = true;
			for (int i = 0; i < n.getArgs().size(); ++i) {
				Expression arg = n.getArgs().get(i);
				if (!first) {
					currentHandler.getPrinter().print(", ");
				}
				arg.accept(currentHandler, context);
				first = false;
			}
		}
		currentHandler.getPrinter().print(")");
		return true;
	}

}
