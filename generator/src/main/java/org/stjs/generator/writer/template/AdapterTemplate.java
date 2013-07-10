package org.stjs.generator.writer.template;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

public class AdapterTemplate implements MethodCallTemplate {
	/**
	 * converts a toFixed(x, 2) => x.toFixed(2)
	 * @param currentHandler
	 * @param n
	 * @param qname
	 * @param context
	 */
	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		Expression arg0 = n.getArgs().get(0);
		// TODO : use parenthesis only if the expression is complex
		currentHandler.getPrinter().print("(");
		arg0.accept(currentHandler, context);
		currentHandler.getPrinter().print(")");
		currentHandler.getPrinter().print(".");
		currentHandler.getPrinter().print(n.getName());
		currentHandler.getPrinter().print("(");
		boolean first = true;
		for (int i = 1; i < n.getArgs().size(); ++i) {
			Expression arg = n.getArgs().get(i);
			if (!first) {
				currentHandler.getPrinter().print(", ");
			}
			arg.accept(currentHandler, context);
			first = false;
		}
		currentHandler.getPrinter().print(")");
		return true;
	}

}
