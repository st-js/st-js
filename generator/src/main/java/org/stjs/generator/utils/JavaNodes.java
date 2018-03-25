package org.stjs.generator.utils;

import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptClassGenerationException;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.javascript.annotation.Native;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * <p>JavaNodes class.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public final class JavaNodes {

	private JavaNodes() {
		// private
	}

	/**
	 * <p>isConstructor.</p>
	 *
	 * @param tree a {@link com.sun.source.tree.Tree} object.
	 * @return a boolean.
	 */
	public static boolean isConstructor(Tree tree) {
		if (!(tree instanceof MethodTree)) {
			return false;
		}
		MethodTree method = (MethodTree) tree;
		return "<init>".equals(method.getName().toString()) && !method.getModifiers().getFlags().contains(Modifier.STATIC);
	}

	/**
	 * <p>sameRawType.</p>
	 *
	 * @param type1 a {@link javax.lang.model.type.TypeMirror} object.
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public static boolean sameRawType(TypeMirror type1, Class<?> clazz) {
		if (!(type1 instanceof DeclaredType)) {
			return false;
		}
		DeclaredType declType1 = (DeclaredType) type1;
		return clazz.getName().equals(((TypeElement) declType1.asElement()).getQualifiedName().toString());
	}

	/**
	 * <p>isStatic.</p>
	 *
	 * @param method a {@link com.sun.source.tree.MethodTree} object.
	 * @return a boolean.
	 */
	public static boolean isStatic(MethodTree method) {
		Set<Modifier> modifiers = method.getModifiers().getFlags();
		return modifiers.contains(Modifier.STATIC);
	}

	/**
	 * <p>isFinal.</p>
	 *
	 * @param tree a {@link com.sun.source.tree.VariableTree} object.
	 * @return a boolean.
	 */
	public static boolean isFinal(VariableTree tree) {
		return tree.getModifiers().getFlags().contains(Modifier.FINAL);
	}

	/**
	 * <p>isStatic.</p>
	 *
	 * @param element a {@link javax.lang.model.element.Element} object.
	 * @return a boolean.
	 */
	public static boolean isStatic(Element element) {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.STATIC);
	}

	/**
	 * <p>isSuper.</p>
	 *
	 * @param expression a {@link com.sun.source.tree.ExpressionTree} object.
	 * @return a boolean.
	 */
	public static boolean isSuper(ExpressionTree expression) {
		if (!(expression instanceof IdentifierTree)) {
			return false;
		}
		return GeneratorConstants.SUPER.equals(((IdentifierTree) expression).getName().toString());
	}

	/**
	 * <p>isInnerType.</p>
	 *
	 * @param type a {@link javax.lang.model.element.Element} object.
	 * @return a boolean.
	 */
	public static boolean isInnerType(Element type) {
		return type.getEnclosingElement().getKind() != ElementKind.PACKAGE;
	}

	/**
	 * <p>getEnclosingType.</p>
	 *
	 * @param type a {@link javax.lang.model.type.TypeMirror} object.
	 * @return a {@link javax.lang.model.type.DeclaredType} object.
	 */
	public static DeclaredType getEnclosingType(TypeMirror type) {
		if (!(type instanceof DeclaredType)) {
			return null;
		}
		DeclaredType declaredType = (DeclaredType) type;

		TypeMirror enclosingType = declaredType.asElement().getEnclosingElement().asType();
		if (enclosingType instanceof ExecutableType) {
			// get the type that encloses this method
			enclosingType = declaredType.asElement().getEnclosingElement().getEnclosingElement().asType();
		}
		if (enclosingType instanceof DeclaredType) {
			return (DeclaredType) enclosingType;
		}
		return null;
	}

	/**
	 * <p>isNative.</p>
	 *
	 * @param element a {@link javax.lang.model.element.Element} object.
	 * @return a boolean.
	 */
	public static boolean isNative(Element element) {
		return element.getModifiers().contains(Modifier.NATIVE) || element.getAnnotation(Native.class) != null;
	}

	/**
	 * <p>isJavaScriptPrimitive.</p>
	 *
	 * @param type a {@link javax.lang.model.type.TypeMirror} object.
	 * @return a boolean.
	 */
	public static boolean isJavaScriptPrimitive(TypeMirror type) {
		return TypesUtils.isPrimitive(type) || TypesUtils.isBoxedPrimitive(type) || TypesUtils.isString(type);
	}

	/**
	 * <p>elementFromDeclaration.</p>
	 *
	 * @param tree a {@link com.sun.source.tree.Tree} object.
	 * @return a {@link javax.lang.model.element.Element} object.
	 */
	public static Element elementFromDeclaration(Tree tree) {
		if (tree instanceof MethodTree) {
			return TreeUtils.elementFromDeclaration((MethodTree) tree);
		}
		if (tree instanceof VariableTree) {
			return TreeUtils.elementFromDeclaration((VariableTree) tree);
		}
		if (tree instanceof ClassTree) {
			return TreeUtils.elementFromDeclaration((ClassTree) tree);
		}
		throw new JavascriptClassGenerationException("none", "Unexpected node type:" + tree.getClass() + "," + tree.getKind());
	}
}
