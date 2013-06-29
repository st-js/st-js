package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * array.$get(x) -> array[x], or $get(obj, prop) -> obj[prop]
 * 
 * @author acraciun
 * 
 */
public class GetTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		if ((n.getArgs() == null) || (n.getArgs().size() < 1) || (n.getArgs().size() > 2)) {
			return false;
		}
		int arg = 0;
		if (n.getArgs().size() == 1) {
			TemplateUtils.printScope(currentHandler, n, context, false);
		} else {
			currentHandler.getPrinter().print("(");
			n.getArgs().get(arg++).accept(currentHandler, context);
			currentHandler.getPrinter().print(")");
		}

		currentHandler.getPrinter().print("[");
		n.getArgs().get(arg).accept(currentHandler, context);
		currentHandler.getPrinter().print("]");
		return true;
	}

}
