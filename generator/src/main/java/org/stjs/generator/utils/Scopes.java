package org.stjs.generator.utils;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

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
	public static boolean isInvokedElementFromOuterType(Element methodElement, GenerationContext<Void> context) {
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

			if (isTypeInClassHierarchy(context, outerTypeErasure, type)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isTypeInClassHierarchy(GenerationContext<Void> context, TypeMirror outerTypeErasure, TypeMirror type) {
		while (true) {
			TypeElement typeElement = ElementUtils.asTypeElement(context, type);

			if (typeElement != null) {
				type = context.getTypes().erasure(typeElement.getSuperclass());
				if (context.getTypes().isSameType(type, outerTypeErasure)) {
					return true;
				}
			} else {
				return false;
			}
		}
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

	public static String buildOuterClassAccessTargetPrefix() {
		return GeneratorConstants.THIS + "." + GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX
				+ GeneratorConstants.INNER_CLASS_CONSTRUCTOR_PARAM_PREFIX;
	}

	public static <JS> List<JS> buildOuterClassParametersAsNames(GenerationContext<JS> context, Element callingClass, Element targetClass) {

		List<JS> result = new ArrayList<>();
		for (String s : buildOuterClassParametersAsString(callingClass, targetClass)) {
			result.add(context.js().name(s));
		}

		return result;
	}

	public static List<String> buildOuterClassParametersAsString(Element callingClass, Element targetClass) {
		int callingClassDeepnessLevel;
		if (callingClass == null) {
			callingClassDeepnessLevel = Integer.MAX_VALUE;
		} else {
			callingClassDeepnessLevel = ElementUtils.getInnerClassDeepnessLevel(callingClass);
		}

		int targetClassDeepnessLevel = ElementUtils.getInnerClassDeepnessLevel(targetClass);

		// Deepness of 0 mean 1 level of deepness
		String prefix = GeneratorConstants.INNER_CLASS_CONSTRUCTOR_PARAM_PREFIX + GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR;

		List<String> outerClassVarNames = new ArrayList<>();
		for (int i = 0; i <= Math.min(callingClassDeepnessLevel, targetClassDeepnessLevel); i++) {
			outerClassVarNames.add(prefix + i);
		}

		// if the target class 1 level deeper thant the calling class? add "this" as the last outerclass parameter
		if (outerClassVarNames.size() <= targetClassDeepnessLevel) {
			outerClassVarNames.add(GeneratorConstants.THIS);
		}

		return outerClassVarNames;
	}

	public static List<String> buildOuterClassAccessPrefixesForDeepness(int maxInnerClassDeepnessId) {
		// Deepness of 0 mean 1 level of deepness
		String prefix = GeneratorConstants.INNER_CLASS_CONSTRUCTOR_PARAM_PREFIX + GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR;

		List<String> outerClassVarNames = new ArrayList<>();
		for (int i = 0; i <= maxInnerClassDeepnessId; i++) {
			outerClassVarNames.add(prefix + i);
		}

		return outerClassVarNames;
	}

	public static boolean isRegularInstanceField(Element fieldElement, IdentifierTree tree) {
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			// only meant for fields
			return false;
		}
		if (JavaNodes.isStatic(fieldElement)) {
			// only instance fieds
			return false;
		}

		if (GeneratorConstants.THIS.equals(tree.getName().toString()) || GeneratorConstants.SUPER.equals(tree.getName().toString())) {
			return false;
		}
		return true;
	}
}
