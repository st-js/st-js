package org.stjs.generator.handlers;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;

import java.util.HashMap;
import java.util.Map;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;
import org.stjs.generator.scope.TypeScope;

/**
 * this is a handler to handle special method names (those starting with $).
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class SpecialMethodHandlers {
	private static final String SPECIAL_PREFIX = "$";

	private Map<String, SpecialMethodHandler> methodHandlers = new HashMap<String, SpecialMethodHandler>();

	public SpecialMethodHandlers() {
		// array.$get(x) -> array[x]
		methodHandlers.put("$get", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {
				if (n.getArgs() == null || n.getArgs().size() != 1) {
					return false;
				}
				printScope(currentHandler, n, qname, context, false);
				currentHandler.getPrinter().print("[");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				currentHandler.getPrinter().print("]");
				return true;
			}
		});

		// array.$set(index, value) -> array[index] = value
		methodHandlers.put("$set", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {
				if (n.getArgs() == null || n.getArgs().size() != 2) {
					return false;
				}
				printScope(currentHandler, n, qname, context, false);
				currentHandler.getPrinter().print("[");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				currentHandler.getPrinter().print("]");
				currentHandler.getPrinter().print(" = ");
				n.getArgs().get(1).accept(currentHandler.getRuleVisitor(), context);
				return true;
			}
		});
		// map.$put(index, value) -> map[index] = value
		methodHandlers.put("$put", methodHandlers.get("$set"));

		// $array() -> []
		methodHandlers.put("$array", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {

				currentHandler.getPrinter().print("[");
				if (n.getArgs() != null) {
					boolean first = true;
					for (Expression arg : n.getArgs()) {
						if (!first) {
							currentHandler.getPrinter().print(", ");
						}
						arg.accept(currentHandler.getRuleVisitor(), context);
						first = false;
					}
				}
				currentHandler.getPrinter().print("]");
				return true;
			}
		});
		// $map() -> {}
		methodHandlers.put("$map", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {

				currentHandler.getPrinter().print("{");
				// TODO -> add constructor args here n.getArgs().get(0).accept(currentHandler.getRuleVisitor(),
				// context);
				currentHandler.getPrinter().print("}");
				return true;
			}
		});

		// equals -> ==
		methodHandlers.put("equals", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {
				if (n.getArgs() == null || n.getArgs().size() != 1) {
					return false;
				}
				printScope(currentHandler, n, qname, context, false);
				currentHandler.getPrinter().print(" == ");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				return true;
			}
		});
	}

	private void printScope(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
			GenerationContext context, boolean withDot) {
		if (qname != null && TypeScope.THIS_SCOPE.equals(qname.getScope())) {
			currentHandler.getPrinter().print("this");
			if (withDot) {
				currentHandler.getPrinter().print(".");
			}
		} else if (n.getScope() != null) {
			n.getScope().accept(currentHandler.getRuleVisitor(), context);
			if (withDot) {
				currentHandler.getPrinter().print(".");
			}
		}

	}

	public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
			GenerationContext context) {

		SpecialMethodHandler handler = methodHandlers.get(n.getName());
		if (handler != null) {
			handler.handle(currentHandler, n, qname, context);
			return true;
		}

		if (n.getName().length() <= SPECIAL_PREFIX.length() || !n.getName().startsWith(SPECIAL_PREFIX)) {
			return false;
		}
		// just transform it in property -> with parameter is an assignment, without a parameter is a simple access.
		// with more parameters -> just skip it
		if (n.getArgs() != null && n.getArgs().size() > 1) {
			return false;
		}

		printScope(currentHandler, n, qname, context, true);
		if (n.getArgs() == null || n.getArgs().size() == 0) {
			currentHandler.getPrinter().print(n.getName().substring(1));
		} else {
			currentHandler.getPrinter().print(n.getName().substring(1));
			currentHandler.getPrinter().print(" = ");
			n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
		}
		return true;
	}

	private interface SpecialMethodHandler {
		public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
				GenerationContext context);
	}
}
