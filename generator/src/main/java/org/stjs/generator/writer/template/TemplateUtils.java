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

	private static void printDot(JavascriptWriterVisitor currentHandler, boolean withDot) {
		if (withDot) {
			currentHandler.getPrinter().print(".");
		}
	}

	public static void printScope(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context,
			boolean withDot) {
		boolean withScopeSuper = n.getScope() != null && n.getScope().toString().equals(GeneratorConstants.SUPER);
		if (withScopeSuper) {
			return;
		}
		MethodWrapper method = resolvedMethod(n);
		boolean withScopeThis = n.getScope() != null && n.getScope().toString().equals(GeneratorConstants.THIS);
		boolean withOtherScope = n.getScope() != null && !withScopeThis;
		if (withOtherScope) {
			n.getScope().accept(currentHandler, context);
			printDot(currentHandler, withDot);
		} else if (!Modifier.isStatic(method.getModifiers())) {
			currentHandler.getPrinter().print(GeneratorConstants.THIS);
			printDot(currentHandler, withDot);
		}
	}
}
