package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * array.$set(index, value) -> array[index] = value, or $set(obj, prop, value) -> obj[prop]=value
 * @author acraciun
 */
public class PutTemplate implements MethodCallTemplate {

	private final static int MIN_ARGS_COUNT = 2;

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		if ((n.getArgs() == null) || (n.getArgs().size() < MIN_ARGS_COUNT) || (n.getArgs().size() > MIN_ARGS_COUNT + 1)) {
			return false;
		}
		int arg = 0;
		if (n.getArgs().size() == 2) {
			TemplateUtils.printScope(currentHandler, n, context, false);
		} else {
			currentHandler.getPrinter().print("(");
			n.getArgs().get(arg++).accept(currentHandler, context);
			currentHandler.getPrinter().print(")");
		}
		currentHandler.getPrinter().print("[");
		n.getArgs().get(arg++).accept(currentHandler, context);
		currentHandler.getPrinter().print("]");
		currentHandler.getPrinter().print(" = ");
		n.getArgs().get(arg++).accept(currentHandler, context);
		return true;
	}

}
