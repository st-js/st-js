package org.stjs.generator.check.declaration;

import java.util.List;


import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.MethodTree;

/**
 * this check verifies that only one method (or constructor) with a given name has actually a body, all the other should
 * be marked as native (or @Native). More the method having the body must be the more generic than the other overloaded
 * methods, so , when generated in the JavaScript, it knows how to handle all the calls.
 * @author acraciun
 */
public class MethodOverloadCheck implements CheckContributor<MethodTree> {
	private static boolean isMoreGenericVarArg(GenerationContext<Void> context, ExecutableElement more, ExecutableElement less) {
		TypeMirror moreType = ((ArrayType) more.getParameters().get(0).asType()).getComponentType();
		for (int p = 0; p < less.getParameters().size(); ++p) {
			TypeMirror lessType = less.getParameters().get(p).asType();
			if (!context.getTypes().isAssignable(lessType, moreType)) {
				// moreType cannot be assigned from lessType
				return false;
			}
		}
		return true;
	}

	private static boolean isMoreGenericNormalArgs(GenerationContext<Void> context, ExecutableElement more, ExecutableElement less) {
		for (int p = 0; p < less.getParameters().size(); ++p) {
			TypeMirror lessType = less.getParameters().get(p).asType();
			TypeMirror moreType = more.getParameters().get(p).asType();
			if (!context.getTypes().isAssignable(lessType, moreType)) {
				// moreType cannot be assigned from lessType
				return false;
			}
		}
		return true;
	}

	/**
	 * return true if the "more" method can be called with arguments that have the type of the "less" method. i.e. is
	 * more generic
	 * @param context
	 * @param more
	 * @param less
	 * @return
	 */
	private static boolean isMoreGeneric(GenerationContext<Void> context, ExecutableElement more, ExecutableElement less, boolean hasVarArgs) {
		if (more.equals(less)) {
			return true;
		}
		// assumes only a var arg parameter is allowed @see checkVarArgs
		if (hasVarArgs) {
			return isMoreGenericVarArg(context, more, less);
		}

		if (less.getParameters().size() > more.getParameters().size()) {
			return false;
		}

		return isMoreGenericNormalArgs(context, more, less);
	}

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		ExecutableElement methodElement = TreeUtils.elementFromDeclaration(tree);
		if (JavaNodes.isNative(methodElement)) {
			// no need to check the native ones - only the one with the body
			return null;
		}
		boolean hasVarArgs = tree.getParameters().size() == 1 && InternalUtils.isVarArg(tree.getParameters().get(0));

		TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
		// for constructors take only the class's other constructors. For regular methods, checks agains all the methods
		// in the class' hierarchy
		List<? extends Element> allMembers =
				methodElement.getKind() == ElementKind.CONSTRUCTOR ? typeElement.getEnclosedElements() : context.getElements().getAllMembers(
						typeElement);

		for (Element memberElement : allMembers) {
			if (methodElement.equals(memberElement)) {
				continue;
			}
			if (!memberElement.getSimpleName().equals(methodElement.getSimpleName()) || memberElement.getKind() != methodElement.getKind()) {
				continue;
			}
			// here I have all the methods with the same name, other than the ckecked method
			if (!isMoreGeneric(context, methodElement, (ExecutableElement) memberElement, hasVarArgs)) {
				context.addError(tree, "There is a method in the class (or one of its parents) having the same name with the method named ["
						+ tree.getName() + "] but is less generic");
			}
		}

		return null;
	}
}
