package org.stjs.generator.utils;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public final class Scopes {
	private Scopes() {
		// private
	}

	/**
	 * True if the method element is invoked from the outer type
	 *
	 * @param methodElement
	 * @param context
     * @return
     */
	public static boolean isInvokedMethodFromOuterType(Element methodElement, GenerationContext<Void> context) {
		ClassTree enclosingClassTree = findEnclosingClassSkipAnonymousInitializer(context.getCurrentPath());

		TypeElement currentScopeClassElement = TreeUtils.elementFromDeclaration(enclosingClassTree);
		TypeElement methodOwnerElement = (TypeElement) methodElement.getEnclosingElement();
		return isOuterType(context, methodOwnerElement, currentScopeClassElement);
	}

	/**
	 * True if outerType is strictly the outer type of the subtype
	 *
	 * @param context
	 * @param outerType
	 * @param subType
     * @return
     */
	public static boolean isOuterType(GenerationContext<Void> context, TypeElement outerType, TypeElement subType) {
		TypeMirror subTypeErasure = context.getTypes().erasure(subType.asType());

		if (!(subTypeErasure instanceof DeclaredType)) {
			return false;
		}

		TypeMirror outerTypeErasure = context.getTypes().erasure(outerType.asType());
		for (TypeMirror type = ((DeclaredType) subTypeErasure).getEnclosingType(); type != null; type = ((DeclaredType) type).getEnclosingType()) {
			if (context.getTypes().isSameType(type, outerTypeErasure)) {
				return true;
			}
			if (!(type instanceof DeclaredType)) {
				return false;
			}
		}
		return false;
	}

	public static ClassTree findEnclosingClassSkipAnonymousInitializer(TreePath path) {
		// if the block is an anonymous initializer, then return the outer class
		TreePath enclosingClassTreePath = TreeUtils.enclosingPathOfType(path, ClassTree.class);
		if (enclosingClassTreePath == null) {
			return null;
		}

		if (isInsideInitializerBlock(path)) {
			// get the outer class
			TreePath outerClassTreePath = TreeUtils.enclosingPathOfType(enclosingClassTreePath.getParentPath(), ClassTree.class);
			if (outerClassTreePath != null) {
				enclosingClassTreePath = outerClassTreePath;
			}
		}
		return (ClassTree) enclosingClassTreePath.getLeaf();
	}

	private static boolean isInsideInitializerBlock(TreePath path) {
		TreePath p = path;
		while (p != null) {
			if (p.getLeaf() instanceof BlockTree && p.getParentPath().getLeaf() instanceof ClassTree) {
				return true;
			}
			if (p.getLeaf() instanceof MethodTree) {
				return false;
			}
			p = p.getParentPath();
		}
		return false;
	}

	public static int getElementDeepnessLevel(Element element) {
		int deepnessLevel = -1;
		Element whileElement = element;
		while (whileElement != null) {
			// Ignore enclosing class and package by default
			if (whileElement.getKind() != ElementKind.PACKAGE && whileElement.getKind() != ElementKind.CLASS) {
				deepnessLevel++;
			}
			whileElement = whileElement.getEnclosingElement();
		}

		return deepnessLevel;
	}
}
