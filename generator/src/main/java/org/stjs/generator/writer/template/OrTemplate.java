package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * $or(x, y, z) -> (x || y || z)
 * @author acraciun
 */
public class OrTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		if ((n.getArgs() == null) || (n.getArgs().size() < 2)) {
			// not exactly what it was expected
			return false;
		}
		currentHandler.getPrinter().print("(");
		n.getArgs().get(0).accept(currentHandler, context);
		for (int i = 1; i < n.getArgs().size(); ++i) {
			currentHandler.getPrinter().print(" || ");
			n.getArgs().get(i).accept(currentHandler, context);
		}
		currentHandler.getPrinter().print(")");
		return true;
	}

}
