/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.handlers;

import static org.stjs.generator.handlers.DumpVisitor.printArguments;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import java.util.HashMap;
import java.util.Map;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.handlers.utils.Option;
import org.stjs.generator.scope.NameType.MethodName;
import org.stjs.generator.scope.QualifiedName;

/**
 * this is a handler to handle special method names (those starting with $).
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class SpecialMethodHandlers {
	private static final String SPECIAL_PREFIX = "$";

	private Map<String, SpecialMethodHandler> methodHandlers = new HashMap<String, SpecialMethodHandler>();

	private final String[] adapterNames = { "org.stjs.javascript.JSNumberAdapter",
			"org.stjs.javascript.JSStringAdapter" };

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
				currentHandler.printer.print("[");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				currentHandler.printer.print("]");
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
				currentHandler.printer.print("[");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				currentHandler.printer.print("]");
				currentHandler.printer.print(" = ");
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

				currentHandler.printer.print("[");
				if (n.getArgs() != null) {
					boolean first = true;
					for (Expression arg : n.getArgs()) {
						if (!first) {
							currentHandler.printer.print(", ");
						}
						arg.accept(currentHandler.getRuleVisitor(), context);
						first = false;
					}
				}
				currentHandler.printer.print("]");
				return true;
			}
		});
		// $map() -> {}
		methodHandlers.put("$map", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {
				if (n.getArgs() != null && n.getArgs().size() > 1) {
					currentHandler.printer.printLn();
					currentHandler.printer.indent();
				}
				currentHandler.printer.print("{");
				if (n.getArgs() != null) {
					boolean first = true;
					for (int i = 0; i < n.getArgs().size(); i += 2) {
						if (!first) {
							currentHandler.printer.print(", ");
							currentHandler.printer.printLn();
						}
						// TODO : is it safe to unsescape string that are object litteral keys?
						n.getArgs().get(i).accept(currentHandler.getRuleVisitor(), context);
						currentHandler.printer.print(": ");
						n.getArgs().get(i + 1).accept(currentHandler.getRuleVisitor(), context);
						first = false;
					}
				}
				if (n.getArgs() != null && n.getArgs().size() > 1) {
					currentHandler.printer.unindent();
				}

				currentHandler.printer.print("}");
				return true;
			}
		});

		// $or(x, y, z) -> (x || y || z)
		methodHandlers.put("$or", new SpecialMethodHandler() {
			@Override
			public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
					GenerationContext context) {
				if (n.getArgs() == null || n.getArgs().size() < 2) {
					// not exactly what it was expected
					return false;
				}
				currentHandler.printer.print("(");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				for (int i = 1; i < n.getArgs().size(); ++i) {
					currentHandler.printer.print(" || ");
					n.getArgs().get(i).accept(currentHandler.getRuleVisitor(), context);
				}
				currentHandler.printer.print(")");
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
				currentHandler.printer.print(" == ");
				n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
				return true;
			}
		});

		methodHandlers.put("$invoke", new $InvokeHandler());
	}

	private static void printScope(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
			GenerationContext context, boolean withDot) {
		// TODO -> handle super
		if (qname != null && !qname.isStatic() && qname.getScope() != null && qname.getScope().isThisScope()) {
			currentHandler.printer.print("this");
			if (withDot) {
				currentHandler.printer.print(".");
			}
		} else if (n.getScope() != null) {
			n.getScope().accept(currentHandler.getRuleVisitor(), context);
			if (withDot) {
				currentHandler.printer.print(".");
			}
		}

	}

	public boolean handleMethodCall(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
			GenerationContext context) {

		SpecialMethodHandler handler = methodHandlers.get(n.getName());
		if (handler != null) {
			handler.handle(currentHandler, n, qname, context);
			return true;
		}

		if (qname != null && qname.getDefinitionPoint().isDefined() && n.getArgs() != null && n.getArgs().size() > 0) {
			for (String adapterName : adapterNames) {
				Option<String> fullyQualifiedString = qname.getDefinitionPoint().getOrThrow().getFullName(true);
				if (fullyQualifiedString.isDefined()) {
					if (fullyQualifiedString.getOrThrow().startsWith(adapterName)) {
						adapterMethod(currentHandler, n, qname, context);
						return true;
					}
				}
			}
		}

		if (n.getName().length() <= SPECIAL_PREFIX.length() || !n.getName().startsWith(SPECIAL_PREFIX)) {
			return false;
		}
		// just transform it in property -> with parameter is an assignment, without a parameter is a simple access.
		// with more parameters -> just skip it
		if (n.getArgs() != null && n.getArgs().size() > 1) {
			return false;
		}
		methodToProperty(currentHandler, n, qname, context);
		return true;
	}

	/**
	 * converts a toFixed(x, 2) => x.toFixed(2)
	 * 
	 * @param currentHandler
	 * @param n
	 * @param qname
	 * @param context
	 */
	private void adapterMethod(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
			GenerationContext context) {
		Expression arg0 = n.getArgs().get(0);
		// TODO : use parenthesis only if the expression is complex
		currentHandler.printer.print("(");
		arg0.accept(currentHandler.getRuleVisitor(), context);
		currentHandler.printer.print(")");
		// TODO may add paranthesis here
		currentHandler.printer.print(".");
		currentHandler.printer.print(n.getName());
		currentHandler.printer.print("(");
		boolean first = true;
		for (int i = 1; i < n.getArgs().size(); ++i) {
			Expression arg = n.getArgs().get(i);
			if (!first) {
				currentHandler.printer.print(", ");
			}
			arg.accept(currentHandler.getRuleVisitor(), context);
			first = false;
		}
		currentHandler.printer.print(")");
	}

	/**
	 * $method() => $method and <br>
	 * $method(x) => $method = x
	 * 
	 * @param currentHandler
	 * @param n
	 * @param qname
	 * @param context
	 */
	private void methodToProperty(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
			GenerationContext context) {
		printScope(currentHandler, n, qname, context, true);
		if (n.getArgs() == null || n.getArgs().size() == 0) {
			currentHandler.printer.print(n.getName().substring(1));
		} else {
			currentHandler.printer.print(n.getName().substring(1));
			currentHandler.printer.print(" = ");
			n.getArgs().get(0).accept(currentHandler.getRuleVisitor(), context);
		}
	}

	static final class $InvokeHandler implements SpecialMethodHandler {
		@Override
		public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
				GenerationContext context) {
			printScope(currentHandler, n, qname, context, false);
			// skip methodname
			printArguments(currentHandler.getRuleVisitor(), currentHandler.printer, n.getArgs(), context);
			return true;
		}
	}

	private interface SpecialMethodHandler {
		public boolean handle(DefaultHandler currentHandler, MethodCallExpr n, QualifiedName<MethodName> qname,
				GenerationContext context);
	}
}
