package org.stjs.generator.writer.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
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
	private static final List<String> VALUEOF_TYPES;
	private static final List<String> PARSE_METHODS;

	static {
		NUMBER_TYPES = new ArrayList<>(Arrays.asList(
				Byte.class.getCanonicalName(),
				Double.class.getCanonicalName(),
				Float.class.getCanonicalName(),
				Integer.class.getCanonicalName(),
				Long.class.getCanonicalName(),
				Short.class.getCanonicalName()
		));
		VALUEOF_TYPES = new ArrayList<>(Arrays.asList(
				String.class.getCanonicalName(),
				Boolean.class.getCanonicalName()
		));
		VALUEOF_TYPES.addAll(NUMBER_TYPES);

		PARSE_METHODS = new ArrayList<>(Arrays.asList(
				"parseInt",
				"parseShort",
				"parseLong",
				"parseByte",
				"parseDouble",
				"parseFloat",
				"isNaN"
		));
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

		boolean isStatic = JavaNodes.isStatic(methodElement);
		String methodOwner = methodElement.getEnclosingElement().toString();

		return isStatic && "valueOf".equals(name) && VALUEOF_TYPES.contains(methodOwner);
	}

	private JS customValueOf(MethodInvocationTree tree, GenerationContext<JS> context, List<JS> arguments) {
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

	private boolean isNumberParsing(MethodInvocationTree tree, String name) {
		Element methodElement = TreeUtils.elementFromUse(tree);

		boolean isStatic = JavaNodes.isStatic(methodElement);
		String methodOwner = methodElement.getEnclosingElement().toString();

		return isStatic && PARSE_METHODS.contains(name) && NUMBER_TYPES.contains(methodOwner);
	}

	private JS customNumberParsing(MethodInvocationTree tree, GenerationContext<JS> context, String name, List<JS> arguments) {
		JavaScriptBuilder<JS> js = context.js();

		if ("parseDouble".equals(name) || "parseFloat".equals(name)) {
			return js.functionCall(js.name("parseFloat"), arguments);
		}

		if ("isNaN".equals(name)) {
			return js.functionCall(js.name("isNaN"), arguments);
		}

		return js.functionCall(js.name("parseInt"), arguments);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		if (isCallToSuperConstructor(tree)) {
			return callToSuperConstructor(visitor, tree, context);
		}

		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree> getCurrentWrapper());
		String name = MethodInvocationWriter.buildMethodName(tree);
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);

		if (isCallToValueOf(tree, name)) {
			return customValueOf(tree, context, arguments);
		}

		if (isNumberParsing(tree, name)) {
			return customNumberParsing(tree, context, name, arguments);
		}

		return context.js().functionCall(context.js().property(target, name), arguments);
	}

}
