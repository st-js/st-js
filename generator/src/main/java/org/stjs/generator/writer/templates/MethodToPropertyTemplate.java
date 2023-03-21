package org.stjs.generator.writer.templates;

import javax.lang.model.element.ExecutableElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * $method() => $method and <br>
 * $method(x) => $method = x and $staticMethod(x) => x.$method and <br>
 * $staticMethod(x, y) => x.$method = y
 *
 * @author acraciun
 */
public class MethodToPropertyTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount > 2) {
			throw context.addError(tree, "A 'toProperty' template can only be applied for methods with 0 or 1 parameters");
		}

		JS target = null;
		int arg = 0;

		// TARGET
		ExecutableElement methodElement = TreeUtils.elementFromUse(tree);
		if (JavaNodes.isStatic(methodElement)) {
			// $staticMethod(x) or $staticMethod(x,y)
			target = context.js().paren(visitor.scan(tree.getArguments().get(arg++), context));
		} else {
			// $method() or $method(x)
			target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree> getCurrentWrapper());
		}

		// NAME
		JS property = context.js().property(target, getPropertyName(tree));

		// VALUE
		if (argCount == arg) {
			// $staticMethod(x) or $method()
			return property;
		}

		// $staticMethod(x,y) or $method(x)
		return context.js().assignment(AssignOperator.ASSIGN, property, visitor.scan(tree.getArguments().get(arg), context));
	}

	public static String getPropertyName(MethodInvocationTree tree) {
		String name = MethodInvocationWriter.buildMethodName(tree);
		int start = name.startsWith("$") ? 1 : 0;
		return name.substring(start);
	}
}
