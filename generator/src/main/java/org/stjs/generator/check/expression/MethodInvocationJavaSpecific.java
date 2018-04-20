package org.stjs.generator.check.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * (c) Swissquote 05.04.18
 *
 * @author sgoetz
 */
public class MethodInvocationJavaSpecific implements CheckContributor<MethodInvocationTree> {
	private static final Map<String, List<String>> STJS_INSTANCE_METHODS;

	static String name(Class clazz) {
		return clazz.getCanonicalName();
	}

	static {
		List<String> numberTypes = new ArrayList<>(Arrays.asList(
				name(Byte.class),
				name(Double.class),
				name(Float.class),
				name(Integer.class),
				name(Long.class),
				name(Short.class)
		));

		List<String> stringType = new ArrayList<>(Arrays.asList(name(String.class)));

		List<String> allTypes = new ArrayList<>();
		allTypes.addAll(stringType);
		allTypes.addAll(numberTypes);
		allTypes.add(name(Boolean.class));

		STJS_INSTANCE_METHODS = new HashMap<>();
		// String, Number, Boolean
		//STJS_INSTANCE_METHODS.put("equals", allTypes); // This is checked separately
		STJS_INSTANCE_METHODS.put("getClass", allTypes);

		// Number methods
		STJS_INSTANCE_METHODS.put("intValue", numberTypes);
		STJS_INSTANCE_METHODS.put("shortValue", numberTypes);
		STJS_INSTANCE_METHODS.put("longValue", numberTypes);
		STJS_INSTANCE_METHODS.put("byteValue", numberTypes);
		STJS_INSTANCE_METHODS.put("floatValue", numberTypes);
		STJS_INSTANCE_METHODS.put("doubleValue", numberTypes);
		STJS_INSTANCE_METHODS.put("isNaN", numberTypes);

		// String methods
		STJS_INSTANCE_METHODS.put("getBytes", stringType); // wasn't implemented in stjs.js anyway
		STJS_INSTANCE_METHODS.put("getChars", stringType); // wasn't implemented in stjs.js anyway
		STJS_INSTANCE_METHODS.put("contentEquals", stringType); // wasn't implemented in stjs.js anyway
		STJS_INSTANCE_METHODS.put("matches", stringType);
		STJS_INSTANCE_METHODS.put("compareTo", stringType);
		STJS_INSTANCE_METHODS.put("compareToIgnoreCase", stringType);
		STJS_INSTANCE_METHODS.put("equalsIgnoreCase", stringType);
		STJS_INSTANCE_METHODS.put("codePointBefore", stringType); // not implemented
		STJS_INSTANCE_METHODS.put("codePointCount", stringType); // not implemented
		STJS_INSTANCE_METHODS.put("replaceAll", stringType);
		STJS_INSTANCE_METHODS.put("replaceFirst", stringType);
		STJS_INSTANCE_METHODS.put("regionMatches", stringType);
		STJS_INSTANCE_METHODS.put("contains", stringType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		String name = MethodInvocationWriter.buildMethodName(tree);
		Element methodElement = TreeUtils.elementFromUse(tree);

		if (GeneratorConstants.SUPER.equals(name) || JavaNodes.isStatic(methodElement)) {
			return null;
		}

		TypeElement methodOwnerElement = (TypeElement) methodElement.getEnclosingElement();

		// If the method isn't used, we're not concerned
		if (!STJS_INSTANCE_METHODS.containsKey(name)) {
			return null;
		}

		List<String> methodImplementations = STJS_INSTANCE_METHODS.get(name);
		if (methodImplementations.contains(methodOwnerElement.toString())) {
			context.addError(tree,
					"You are trying to call a method that exists only in Java. Called '" + methodOwnerElement.getSimpleName().toString()
							+ ".prototype." + name + "()'");
		}

		return null;
	}
}
