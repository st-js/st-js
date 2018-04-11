package org.stjs.generator.writer.templates;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.TreeUtils;
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

	@Override
	public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		if (isCallToSuperConstructor(tree)) {
			return callToSuperConstructor(visitor, tree, context);
		}

		JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree> getCurrentWrapper());
		String name = MethodInvocationWriter.buildMethodName(tree);
		List<JS> arguments = MethodInvocationWriter.buildArguments(visitor, tree, context);
		return context.js().functionCall(context.js().property(target, name), arguments);
	}
}
