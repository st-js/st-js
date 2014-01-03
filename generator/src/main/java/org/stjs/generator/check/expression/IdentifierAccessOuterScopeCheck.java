package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.STJSRuntimeException;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;

public class IdentifierAccessOuterScopeCheck implements CheckContributor<IdentifierTree> {
	/**
	 * if the block is an anonymous initializer, then return the outer class
	 * 
	 * @param element
	 * @return
	 */
	public static TypeElement getEnclosingElementSkipAnonymousInitializer(TypeElement element) {
		if (element.getNestingKind() != NestingKind.ANONYMOUS) {
			return element;
		}
		return element.getSuperclass() instanceof DeclaredType ? (TypeElement) ((DeclaredType) element.getSuperclass()).asElement() : element;
	}

	public static boolean isSubtype(GenerationContext<Void> context, TypeElement currentScopeClassElement, TypeElement ownerElement) {
		return context.getTypes().isSubtype(context.getTypes().erasure(currentScopeClassElement.asType()),
				context.getTypes().erasure(ownerElement.asType()));
	}

	@Override
	public Void visit(CheckVisitor visitor, IdentifierTree tree, GenerationContext<Void> context) {
		Element fieldElement = TreeUtils.elementFromUse(tree);
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			// only meant for fields
			return null;
		}
		if (JavaNodes.isStatic(fieldElement)) {
			// only instance fieds
			return null;
		}

		if (GeneratorConstants.THIS.equals(tree.getName().toString()) || GeneratorConstants.SUPER.equals(tree.getName().toString())) {
			return null;
		}
		try {
			ClassTree enclosingClassTree = TreeUtils.enclosingClass(context.getCurrentPath());

			TypeElement currentScopeClassElement = getEnclosingElementSkipAnonymousInitializer(TreeUtils
					.elementFromDeclaration(enclosingClassTree));
			TypeElement fieldOwnerElement = (TypeElement) fieldElement.getEnclosingElement();
			if (!isSubtype(context, currentScopeClassElement, fieldOwnerElement)) {
				context.addError(
						tree,
						"In Javascript you cannot access a field from the outer type. "
								+ "You should define a variable var that=this outside your function definition and use the property of this object. The field: "
								+ tree);
			}
		} catch (Throwable e) {
			throw new STJSRuntimeException("Error gettting scope from:" + tree, e);
		}

		return null;
	}
}
