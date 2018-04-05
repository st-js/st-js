package org.stjs.generator.check.expression;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MethodInvocationTree;

/**
 * (c) Swissquote 05.04.18
 *
 * @author sgoetz
 */
public class MethodInvocationEnumCheck implements CheckContributor<MethodInvocationTree> {

	public boolean checkEnum(MethodInvocationTree tree, GenerationContext<Void> context, TypeElement element, String methodName) {
		if ("java.lang.Enum".equals(element.toString())) {
			context.addError(tree, "In TypeScript you cannot call methods on enums. Called '." + methodName + "()'");
			return true;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		String name = MethodInvocationWriter.buildMethodName(tree);

		if (GeneratorConstants.SUPER.equals(name)) {
			return null;
		}

		Element methodElement = InternalUtils.symbol(tree);
		TypeElement methodOwnerElement = (TypeElement) methodElement.getEnclosingElement();

		// Enum values directly inherit from Enum
		if (checkEnum(tree, context, methodOwnerElement, name)) {
			return null;
		}

		// Static methods are on the enum type, so we need to find its super types
		List<TypeElement> superTypes = ElementUtils.getSuperTypes(methodOwnerElement, false);
		for (TypeElement superType : superTypes) {
			if (checkEnum(tree, context, superType, name)) {
				return null;
			}
		}

		return null;
	}
}
