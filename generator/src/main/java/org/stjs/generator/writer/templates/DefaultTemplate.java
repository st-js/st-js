package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * this is the standard generation template
 *
 * @author acraciun
 */
public class DefaultTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	private static final List<String> NUMBER_TYPES;
	private static final List<String> BASIC_TYPES;
	private static final List<String> STATIC_NUMBER_METHODS;
	private static final List<String> PROTOTYPE_NUMBER_METHODS;

	static {
		NUMBER_TYPES = new ArrayList<>(Arrays.asList(
				Byte.class.getCanonicalName(),
				Double.class.getCanonicalName(),
				Float.class.getCanonicalName(),
				Integer.class.getCanonicalName(),
				Long.class.getCanonicalName(),
				Short.class.getCanonicalName()
		));
		BASIC_TYPES = new ArrayList<>(Arrays.asList(
				String.class.getCanonicalName(),
				Boolean.class.getCanonicalName()
		));
		BASIC_TYPES.addAll(NUMBER_TYPES);

		STATIC_NUMBER_METHODS = new ArrayList<>(Arrays.asList(
				"parseInt",
				"parseShort",
				"parseLong",
				"parseByte",
				"parseDouble",
				"parseFloat",
				"isNaN"
		));

		PROTOTYPE_NUMBER_METHODS = new ArrayList<>(Arrays.asList(
				"intValue",
				"shortValue",
				"longValue",
				"byteValue",
				"floatValue",
				"doubleValue",
				"isNaN"
		));
	}

	public static boolean isConvertedEquals(String owner) {
		return BASIC_TYPES.contains(owner);
	}

	private boolean isCallToSuperConstructor(MethodInvocationTree tree) {
		if (!TreeUtils.isSuperCall(tree)) {
			return false;
		}

		Element methodElement = TreeUtils.elementFromUse(tree);
		if (JavaNodes.isStatic(methodElement)) {
			// this is a call of type super.staticMethod(args) &gt; it should be handled as a simple call to staticMethod
			return false;
		}

		return true;
	}

	/**
	 * super(args) &gt; SuperType.call(this, args)
	 */
	private JS callToSuperConstructor(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		Element methodElement = TreeUtils.elementFromUse(tree);
		TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();

		String methodName = MethodInvocationWriter.buildMethodName(tree);

		// avoid useless call to super() when the super class is Object
		if (GeneratorConstants.SUPER.equals(methodName) && JavaNodes.sameRawType(typeElement.asType(), Object.class)) {
			return null;
		}

		// avoid call to super for synthetic types
		if (GeneratorConstants.SUPER.equals(methodName) && context.getCurrentWrapper().getEnclosingType().isSyntheticType()) {
			return null;
		}

		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		String name = MethodInvocationWriter.buildMethodName(tree);

		JS leftSide;
		if (GeneratorConstants.SUPER.equalsIgnoreCase(name)) {
			leftSide = context.js().name(name);
		} else {
			leftSide = context.js().property(context.js().name("super"), name);
		}

		return context.js().functionCall(leftSide, arguments);
	}

	public boolean isCallToValueOf(MethodInvocationTree tree, String name) {
		Element methodElement = TreeUtils.elementFromUse(tree);
		String methodOwner = methodElement.getEnclosingElement().toString();

		return "valueOf".equals(name) && isConvertedEquals(methodOwner);
	}

	private JS convertedValueOf(MethodInvocationTree tree, GenerationContext<JS> context, List<JS> arguments) {
		JavaScriptBuilder<JS> js = context.js();

		Element methodElement = TreeUtils.elementFromUse(tree);
		String methodOwner = methodElement.getEnclosingElement().toString();

		if (String.class.getCanonicalName().equals(methodOwner)) {
			List<JS> args = new ArrayList<>();
			args.add(js.string(""));
			args.add(arguments.get(0));
			return js.binary(BinaryOperator.PLUS, args);
		}

		if (Boolean.class.getCanonicalName().equals(methodOwner)) {
			return js.unary(UnaryOperator.LOGICAL_COMPLEMENT, js.unary(UnaryOperator.LOGICAL_COMPLEMENT, arguments.get(0)));
		}

		JS newNumber = js.newExpression(js.name("Number"), arguments);
		return js.functionCall(js.property(newNumber, "valueOf"), new ArrayList<JS>());
	}

	private boolean isJavaNumberMethod(MethodInvocationTree tree, String name) {
		Element methodElement = TreeUtils.elementFromUse(tree);

		boolean isStatic = JavaNodes.isStatic(methodElement);
		String methodOwner = methodElement.getEnclosingElement().toString();

		if (!NUMBER_TYPES.contains(methodOwner)) {
			return false;
		}

		return isStatic && STATIC_NUMBER_METHODS.contains(name) || !isStatic && PROTOTYPE_NUMBER_METHODS.contains(name);
	}

	private JS convertedNumberMethod(GenerationContext<JS> context, String name, List<JS> arguments) {
		JavaScriptBuilder<JS> js = context.js();

		if ("parseDouble".equals(name) || "parseFloat".equals(name) || "floatValue".equals(name) || "doubleValue".equals(name)) {
			return js.functionCall(js.name("parseFloat"), arguments);
		}

		if ("isNaN".equals(name)) {
			return js.functionCall(js.name("isNaN"), arguments);
		}

		return js.functionCall(js.name("parseInt"), arguments);
	}

	private boolean isEquals(String name, Element methodElement) {
		if (!"equals".equals(name)) {
			return false;
		}

		String methodOwner = methodElement.getEnclosingElement().toString();

		return BASIC_TYPES.contains(methodOwner);
	}

	private boolean hasUnaryNotAround(GenerationContext<JS> context) {
		Tree parent = context.getCurrentWrapper().getPath().getParentPath().getLeaf();

		if (!(parent instanceof UnaryTree)) {
			return false;
		}

		UnaryOperator op = UnaryOperator.valueOf(parent.getKind());
		return UnaryOperator.LOGICAL_COMPLEMENT.equals(op);
	}

	private JS convertEquals(GenerationContext<JS> context, JS left, JS right) {
		JavaScriptBuilder<JS> js = context.js();
		BinaryOperator operator = hasUnaryNotAround(context) ? BinaryOperator.NOT_EQUAL_TO : BinaryOperator.EQUAL_TO;

		return js.binary(operator, Arrays.asList(left, right));
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		if (isCallToSuperConstructor(tree)) {
			return callToSuperConstructor(visitor, tree, context);
		}

		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree> getCurrentWrapper());
		String name = MethodInvocationWriter.buildMethodName(tree);
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		Element methodElement = TreeUtils.elementFromUse(tree);

		boolean isStatic = JavaNodes.isStatic(methodElement);

		// Replace `Boolean.valueOf(foo)` replaced by `!!foo`
		// Replace `String.valueOf(foo)` replaced by `"" + foo`
		// Replace `Integer.valueOf(foo)` replaced by `new Number(foo).valueOf()`
		if (isStatic && isCallToValueOf(tree, name)) {
			return convertedValueOf(tree, context, arguments);
		}
		// Replace `Integer.parseInt()`, `Short.parseShort()`, `Float.parseFloat()`, ... replaced by `parseInt()` and `parseFloat()`
		// Replace `Integer.prototype.shortValue()`, `Short.prototype.intValue()`, ... replaced by `parseInt()` and `parseFloat()`
		if (isJavaNumberMethod(tree, name)) {
			return convertedNumberMethod(context, name, isStatic ? arguments : Arrays.asList(target));
		}

		if (!isStatic) {
			// Replace `foo.equals(bar)` with `foo == bar`
			// Replace `!foo.equals(bar)` with `foo != bar`
			if (isEquals(name, methodElement)) {
				return convertEquals(context, target, arguments.get(0));
			}

			String methodOwner = methodElement.getEnclosingElement().toString();
			if (String.class.getCanonicalName().equals(methodOwner)) {
				// Replace `foo.replaceFirst("bar", "baz")` with `foo.replace(new RegExp("bar"), "baz")`
				// Replace `foo.replaceAll("bar", "baz")` with `foo.replace(new RegExp("bar", "g"), "baz")`
				if ("replaceAll".equals(name) || "replaceFirst".equals(name)) {
					return convertReplace(context, name, target, arguments);
				}

				// Replace `foo.contains(bar)` with `foo.indexOf(bar) >= 0`
				if ("contains".equals(name)) {
					return convertContains(context, target, arguments);
				}

				// Replace `foo.matches(bar)` with `foo.match("^" + regexp + "$") != null`
				if ("matches".equals(name)) {
					return convertMatches(context, target, arguments);
				}
			}
		}

		return context.js().functionCall(context.js().property(target, name), arguments);
	}

	private JS convertMatches(GenerationContext<JS> context, JS target, List<JS> arguments) {
		JavaScriptBuilder<JS> js = context.js();

		JS matchArg = js.binary(BinaryOperator.PLUS, Arrays.asList(js.string("^"), arguments.get(0), js.string("$")));
		JS matchCall = js.functionCall(js.property(target, "match"), Arrays.asList(matchArg));

		return js.binary(BinaryOperator.NOT_EQUAL_TO, Arrays.asList(matchCall, js.keyword(Keyword.NULL)));
	}

	private JS convertContains(GenerationContext<JS> context, JS target, List<JS> arguments) {
		JavaScriptBuilder<JS> js = context.js();

		JS containsCall = js.functionCall(js.property(target, "indexOf"), arguments);
		return js.binary(BinaryOperator.GREATER_THAN_EQUAL, Arrays.asList(containsCall, js.number(0)));
	}

	private JS convertReplace(GenerationContext<JS> context, String name, JS target, List<JS> arguments) {
		JavaScriptBuilder<JS> js = context.js();

		List<JS> newRegexArguments = new ArrayList<>(Arrays.asList(arguments.get(0)));

		if ("replaceAll".equals(name)) {
			newRegexArguments.add(js.string("g"));
		}

		JS newRegex = js.newExpression(js.name("RegExp"), newRegexArguments);
		return js.functionCall(js.property(target, "replace"), Arrays.asList(newRegex, arguments.get(1)));
	}
}
