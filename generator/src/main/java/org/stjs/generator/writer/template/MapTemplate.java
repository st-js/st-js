package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.scope.Checks;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * $map() -> {}
 * @author acraciun
 */
public class MapTemplate implements MethodCallTemplate {

	private void writeArgs(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		boolean first = true;
		for (int i = 0; i < n.getArgs().size(); i += 2) {
			if (!first) {
				currentHandler.getPrinter().print(", ");
				currentHandler.getPrinter().printLn();
			}
			n.getArgs().get(i).accept(currentHandler, context);
			currentHandler.getPrinter().print(": ");
			n.getArgs().get(i + 1).accept(currentHandler, context);
			first = false;
		}
	}

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		boolean hasArgs = (n.getArgs() != null) && (n.getArgs().size() > 1);
		if (hasArgs) {
			currentHandler.getPrinter().indent();
		}
		currentHandler.getPrinter().print("{");
		if (n.getArgs() != null) {
			Checks.checkMapConstructor(n, context);
			writeArgs(currentHandler, n, context);
		}
		if (hasArgs) {
			currentHandler.getPrinter().unindent();
		}

		currentHandler.getPrinter().print("}");
		return true;

	}

}
