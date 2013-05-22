package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * map.$delete(key) -> delete map[key]
 */
public class DeleteTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		if ((n.getArgs() == null) || (n.getArgs().size() != 1)) {
			return false;
		}
		currentHandler.getPrinter().print("delete ");
		TemplateUtils.printScope(currentHandler, n, context, false);
		currentHandler.getPrinter().print("[");
		n.getArgs().get(0).accept(currentHandler, context);
		currentHandler.getPrinter().print("]");
		return true;
	}

}
