package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.StringLiteralExpr;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.writer.JavascriptWriterVisitor;

public class JsTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		if ((n.getArgs() == null) || (n.getArgs().size() != 1)) {
			return false;
		}
		if (!(n.getArgs().get(0) instanceof StringLiteralExpr)) {
			throw new JavascriptFileGenerationException(context.getInputFile(), new SourcePosition(n),
					"$js can be used only with string literals");
		}
		StringLiteralExpr code = (StringLiteralExpr) n.getArgs().get(0);
		currentHandler.getPrinter().print(code.getValue());
		return true;
	}

}
