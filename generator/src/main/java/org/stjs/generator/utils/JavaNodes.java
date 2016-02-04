package org.stjs.generator.utils;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptClassGenerationException;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.javascript.annotation.Native;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

public final class JavaNodes {

	private JavaNodes() {
		// private
	}

	public static boolean isConstructor(Tree tree) {
		if (!(tree instanceof MethodTree)) {
			return false;
		}
		MethodTree method = (MethodTree) tree;
		return "<init>".equals(method.getName().toString()) && !method.getModifiers().getFlags().contains(Modifier.STATIC);
	}

	public static boolean sameRawType(TypeMirror type1, Class<?> clazz) {
		if (!(type1 instanceof DeclaredType)) {
			return false;
		}
		DeclaredType declType1 = (DeclaredType) type1;
		return clazz.getName().equals(((TypeElement) declType1.asElement()).getQualifiedName().toString());
	}

	public static boolean isStatic(MethodTree method) {
		Set<Modifier> modifiers = method.getModifiers().getFlags();
		return modifiers.contains(Modifier.STATIC);
	}

	public static boolean isPublic(MethodTree method) {
		Set<Modifier> modifiers = method.getModifiers().getFlags();
		return modifiers.contains(Modifier.PUBLIC);
	}

	public static boolean isFinal(VariableTree tree) {
		return tree.getModifiers().getFlags().contains(Modifier.FINAL);
	}

	public static boolean isStatic(Element element) {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.STATIC);
	}

	public static boolean isSuper(ExpressionTree expression) {
		if (!(expression instanceof IdentifierTree)) {
			return false;
		}
		return GeneratorConstants.SUPER.equals(((IdentifierTree) expression).getName().toString());
	}

	public static boolean isInnerType(Element type) {
		return type.getEnclosingElement().getKind() != ElementKind.PACKAGE;
	}

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

	public static boolean isNative(Element element) {
		return element.getModifiers().contains(Modifier.NATIVE) || element.getAnnotation(Native.class) != null;
	}

	public static boolean isJavaScriptPrimitive(TypeMirror type) {
		return TypesUtils.isPrimitive(type) || TypesUtils.isBoxedPrimitive(type) || TypesUtils.isString(type);
	}

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
