package org.stjs.generator.writer.template;

import static org.stjs.generator.ast.ASTNodeData.resolvedMethod;
import japa.parser.ast.expr.MethodCallExpr;

import java.lang.reflect.Modifier;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.writer.JavascriptWriterVisitor;

public final class TemplateUtils {
	private TemplateUtils() {
		//
	}

	public static void printScope(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context,
			boolean withDot) {
		MethodWrapper method = resolvedMethod(n);
		boolean withScopeThis = n.getScope() != null && n.getScope().toString().equals(GeneratorConstants.THIS);
		boolean withScopeSuper = n.getScope() != null && n.getScope().toString().equals(GeneratorConstants.SUPER);
		if (!withScopeSuper) {
			if (n.getScope() != null && !withScopeThis) {
				n.getScope().accept(currentHandler, context);
				if (withDot) {
					currentHandler.getPrinter().print(".");
				}
			} else if (!Modifier.isStatic(method.getModifiers())) {
				currentHandler.getPrinter().print("this");
				if (withDot) {
					currentHandler.getPrinter().print(".");
				}
			}
		}
	}
}
