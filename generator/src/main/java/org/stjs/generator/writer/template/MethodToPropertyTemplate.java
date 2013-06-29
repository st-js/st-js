package org.stjs.generator.writer.template;

import japa.parser.ast.expr.MethodCallExpr;

import java.lang.reflect.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.writer.JavascriptWriterVisitor;

/**
 * $method() => $method and <br>
 * $method(x) => $method = x and $staticMethod(x) => x.$method and <br>
 * $staticMethod(x, y) => x.$method = y
 * 
 * @author acraciun
 * 
 */
public class MethodToPropertyTemplate implements MethodCallTemplate {

	@Override
	public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		int arg = 0;
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
		if (Modifier.isStatic(method.getModifiers())) {
			currentHandler.getPrinter().print("(");
			n.getArgs().get(arg++).accept(currentHandler, context);
			currentHandler.getPrinter().print(").");
		} else {
			TemplateUtils.printScope(currentHandler, n, context, true);
		}
		int start = n.getName().startsWith("$") ? 1 : 0;
		if ((n.getArgs() == null) || (n.getArgs().size() == arg)) {
			currentHandler.getPrinter().print(n.getName().substring(start));
		} else {
			currentHandler.getPrinter().print(n.getName().substring(start));
			currentHandler.getPrinter().print(" = ");
			n.getArgs().get(arg).accept(currentHandler, context);
		}
		return true;
	}

}
