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
package org.stjs.generator.writer;

import static org.stjs.generator.ast.ASTNodeData.resolvedMethod;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.StringLiteralExpr;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptGenerationException;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.ast.SourcePosition;
import org.stjs.generator.scope.Checks;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.utils.ClassUtils;

/**
 * this is a handler to handle special method names (those starting with $).
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 */
public class SpecialMethodHandlers {
	/**
	 * 
	 * allowed templates
	 * 
	 */
	public enum Template {
		none;
	}

	private static final String SPECIAL_PREFIX = "$";

	private static final String ASSERT_PREFIX = "assert";

	private Map<String, SpecialMethodHandler> methodHandlers = new HashMap<String, SpecialMethodHandler>();
	private Map<String, SpecialMethodHandler> methodTemplates = new HashMap<String, SpecialMethodHandler>();

	private final AssertHandler assertHandler;
	private final MethodToPropertyHandler methodToPropertyHandler;
	private final $InvokeHandler invokeHandler;

	public SpecialMethodHandlers() {
		// array.$get(x) -> array[x], or $get(obj, prop) -> obj[prop]
		methodHandlers.put("$get", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() < 1) || (n.getArgs().size() > 2)) {
					return false;
				}
				int arg = 0;
				if (n.getArgs().size() == 1) {
					printScope(currentHandler, n, context, false);
				} else {
					currentHandler.printer.print("(");
					n.getArgs().get(arg++).accept(currentHandler, context);
					currentHandler.printer.print(")");
				}

				currentHandler.printer.print("[");
				n.getArgs().get(arg++).accept(currentHandler, context);
				currentHandler.printer.print("]");
				return true;
			}
		});

		// array.$set(index, value) -> array[index] = value, or $set(obj, prop, value) -> obj[prop]=value
		methodHandlers.put("$set", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() < 2) || (n.getArgs().size() > 3)) {
					return false;
				}
				int arg = 0;
				if (n.getArgs().size() == 2) {
					printScope(currentHandler, n, context, false);
				} else {
					currentHandler.printer.print("(");
					n.getArgs().get(arg++).accept(currentHandler, context);
					currentHandler.printer.print(")");
				}
				currentHandler.printer.print("[");
				n.getArgs().get(arg++).accept(currentHandler, context);
				currentHandler.printer.print("]");
				currentHandler.printer.print(" = ");
				n.getArgs().get(arg++).accept(currentHandler, context);
				return true;
			}
		});
		// map.$put(index, value) -> map[index] = value
		methodHandlers.put("$put", methodHandlers.get("$set"));

		// map.$delete(key) -> delete map[key]
		methodHandlers.put("$delete", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() != 1)) {
					return false;
				}
				currentHandler.printer.print("delete ");
				printScope(currentHandler, n, context, false);
				currentHandler.printer.print("[");
				n.getArgs().get(0).accept(currentHandler, context);
				currentHandler.printer.print("]");
				return true;
			}
		});

		// $array() -> []
		methodHandlers.put("$array", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {

				currentHandler.printer.print("[");
				if (n.getArgs() != null) {
					boolean first = true;
					for (Expression arg : n.getArgs()) {
						if (!first) {
							currentHandler.printer.print(", ");
						}
						arg.accept(currentHandler, context);
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
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() != null) && (n.getArgs().size() > 1)) {
					// currentHandler.printer.printLn();
					currentHandler.printer.indent();
				}
				currentHandler.printer.print("{");
				if (n.getArgs() != null) {
					Checks.checkMapConstructor(n, context);
					boolean first = true;
					for (int i = 0; i < n.getArgs().size(); i += 2) {
						if (!first) {
							currentHandler.printer.print(", ");
							currentHandler.printer.printLn();
						}
						n.getArgs().get(i).accept(currentHandler, context);
						currentHandler.printer.print(": ");
						n.getArgs().get(i + 1).accept(currentHandler, context);
						first = false;
					}
				}
				if ((n.getArgs() != null) && (n.getArgs().size() > 1)) {
					currentHandler.printer.unindent();
				}

				currentHandler.printer.print("}");
				return true;
			}
		});

		// $or(x, y, z) -> (x || y || z)
		methodHandlers.put("$or", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() < 2)) {
					// not exactly what it was expected
					return false;
				}
				currentHandler.printer.print("(");
				n.getArgs().get(0).accept(currentHandler, context);
				for (int i = 1; i < n.getArgs().size(); ++i) {
					currentHandler.printer.print(" || ");
					n.getArgs().get(i).accept(currentHandler, context);
				}
				currentHandler.printer.print(")");
				return true;
			}
		});

		// equals -> ==
		methodHandlers.put("equals", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() != 1)) {
					return false;
				}
				currentHandler.printer.print("(");
				printScope(currentHandler, n, context, false);
				currentHandler.printer.print(" == ");
				n.getArgs().get(0).accept(currentHandler, context);
				currentHandler.printer.print(")");
				return true;
			}
		});

		// $properties(obj) -> obj
		methodHandlers.put("$properties", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() != 1)) {
					return false;
				}
				currentHandler.printer.print("(");
				n.getArgs().get(0).accept(currentHandler, context);
				currentHandler.printer.print(")");
				return true;
			}
		});
		methodHandlers.put("$object", methodHandlers.get("$properties"));
		methodHandlers.put("$castArray", methodHandlers.get("$properties"));

		methodHandlers.put("$js", new SpecialMethodHandler() {
			@Override
			public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
				if ((n.getArgs() == null) || (n.getArgs().size() != 1)) {
					return false;
				}
				if (!(n.getArgs().get(0) instanceof StringLiteralExpr)) {
					throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
							"$js can be used only with string literals");
				}
				StringLiteralExpr code = (StringLiteralExpr) n.getArgs().get(0);
				currentHandler.printer.print(code.getValue());
				return true;
			}
		});

		assertHandler = new AssertHandler();
		methodToPropertyHandler = new MethodToPropertyHandler();
		invokeHandler = new $InvokeHandler();

		methodHandlers.put("java.lang.String.length", methodToPropertyHandler);

		// the new way -> templates
		methodTemplates.put(Template.none.name(), new DoNothingHandler());
	}

	private static void printScope(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context,
			boolean withDot) {
		MethodWrapper method = resolvedMethod(n);
		boolean withScopeThis = n.getScope() != null && n.getScope().toString().equals(GeneratorConstants.THIS);
		boolean withScopeSuper = n.getScope() != null && n.getScope().toString().equals(GeneratorConstants.SUPER);
		if (n.getScope() != null && !withScopeSuper && !withScopeThis) {
			n.getScope().accept(currentHandler, context);
			if (withDot) {
				currentHandler.printer.print(".");
			}
		} else if (!withScopeSuper && !Modifier.isStatic(method.getModifiers())) {
			currentHandler.printer.print("this");
			if (withDot) {
				currentHandler.printer.print(".");
			}
		}
	}

	public boolean handleMethodCall(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
		org.stjs.javascript.annotation.Template templateAnn = method
				.getAnnotation(org.stjs.javascript.annotation.Template.class);
		if (templateAnn != null) {
			SpecialMethodHandler handler = methodTemplates.get(templateAnn.value());
			if (handler == null) {
				throw new JavascriptGenerationException(context.getInputFile(), new SourcePosition(n),
						"The template named '" + templateAnn.value() + " was not found");
			}
			return handler.handle(currentHandler, n, context);
		}

		String fullName = method.getOwnerType().getName() + "." + method.getName();

		SpecialMethodHandler handler = methodHandlers.get(fullName);
		if (handler == null) {
			handler = methodHandlers.get(n.getName());
		}
		if (handler != null) {
			if (handler.handle(currentHandler, n, context)) {
				return true;
			}
		}

		if (n.getName().startsWith(ASSERT_PREFIX)) {
			assertHandler.handle(currentHandler, n, context);
			return true;
		}

		if (ClassUtils.isJavascriptFunction(method.getOwnerType())) {
			invokeHandler.handle(currentHandler, n, context);
			return true;
		}

		if ((n.getName().length() > SPECIAL_PREFIX.length()) && n.getName().startsWith(SPECIAL_PREFIX)) {
			// just transform it in property -> with parameter is an assignment, without a parameter is a simple access.
			// with more parameters -> just skip it
			// WARNING $ alone should be kept for jquery and alike
			if ((n.getArgs() == null) || (n.getArgs().size() <= 2)) {
				return methodToPropertyHandler.handle(currentHandler, n, context);
			}
		}
		if ((n.getArgs() != null) && (n.getArgs().size() > 0)) {
			if (ClassUtils.isAdapter(method.getOwnerType())) {
				adapterMethod(currentHandler, n, context);
				return true;
			}
		}
		return false;
	}

	/**
	 * converts a toFixed(x, 2) => x.toFixed(2)
	 * 
	 * @param currentHandler
	 * @param n
	 * @param qname
	 * @param context
	 */
	private void adapterMethod(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
		Expression arg0 = n.getArgs().get(0);
		// TODO : use parenthesis only if the expression is complex
		currentHandler.printer.print("(");
		arg0.accept(currentHandler, context);
		currentHandler.printer.print(")");
		currentHandler.printer.print(".");
		currentHandler.printer.print(n.getName());
		currentHandler.printer.print("(");
		boolean first = true;
		for (int i = 1; i < n.getArgs().size(); ++i) {
			Expression arg = n.getArgs().get(i);
			if (!first) {
				currentHandler.printer.print(", ");
			}
			arg.accept(currentHandler, context);
			first = false;
		}
		currentHandler.printer.print(")");
	}

	/**
	 * $method() => $method and <br>
	 * $method(x) => $method = x and $staticMethod(x) => x.$method and <br>
	 * $staticMethod(x, y) => x.$method = y
	 * 
	 * @param currentHandler
	 * @param n
	 * @param qname
	 * @param context
	 */
	static final class MethodToPropertyHandler implements SpecialMethodHandler {
		@Override
		public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
			int arg = 0;
			MethodWrapper method = ASTNodeData.resolvedMethod(n);
			if (Modifier.isStatic(method.getModifiers())) {
				currentHandler.printer.print("(");
				n.getArgs().get(arg++).accept(currentHandler, context);
				currentHandler.printer.print(").");
			} else {
				printScope(currentHandler, n, context, true);
			}
			int start = n.getName().startsWith("$") ? 1 : 0;
			if ((n.getArgs() == null) || (n.getArgs().size() == arg)) {
				currentHandler.printer.print(n.getName().substring(start));
			} else {
				currentHandler.printer.print(n.getName().substring(start));
				currentHandler.printer.print(" = ");
				n.getArgs().get(arg++).accept(currentHandler, context);
			}
			return true;
		}
	}

	static final class $InvokeHandler implements SpecialMethodHandler {
		@Override
		public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
			printScope(currentHandler, n, context, false);
			// skip methodname
			currentHandler.printArguments(n.getArgs(), context);
			return true;
		}
	}

	static final class AssertHandler implements SpecialMethodHandler {
		@Override
		public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
			printScope(currentHandler, n, context, false);
			currentHandler.printer.print(n.getName());
			String location = "\"" + context.getInputFile().getName() + ":" + n.getBeginLine() + "\"";
			String params = "\"" + n.toString().replace("\"", "\\\"") + "\"";
			currentHandler.printArguments(Arrays.asList(location, params), n.getArgs(),
					Collections.<String> emptyList(), context);
			return true;
		}
	}

	static final class DoNothingHandler implements SpecialMethodHandler {
		@Override
		public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
			return false;
		}
	}

	private interface SpecialMethodHandler {
		public boolean handle(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context);
	}
}
