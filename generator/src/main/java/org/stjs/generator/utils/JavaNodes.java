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
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

public class JavaNodes {
	public static boolean isConstructor(Tree tree) {
		if (!(tree instanceof MethodTree)) {
			return false;
		}
		MethodTree method = (MethodTree) tree;
		return method.getName().toString().equals("<init>") && !method.getModifiers().getFlags().contains(Modifier.STATIC);
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

	public static boolean isJavaScriptFunction(Element element) {
		return element.getAnnotation(JavascriptFunction.class) != null;
	}

	public static boolean isGlobal(Element element) {
		return element.getAnnotation(GlobalScope.class) != null;
	}

	public static String getNamespace(Element type) {
		Namespace ns = type.getAnnotation(Namespace.class);
		if (ns != null) {
			return ns.value();
		}
		return null;
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

	public static String getMethodTemplate(Element element) {
		Template t = element.getAnnotation(Template.class);
		if (t != null) {
			return t.value();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static boolean isSyntheticType(Element type) {
		return type.getAnnotation(SyntheticType.class) != null || type.getAnnotation(DataType.class) != null;
	}

	public static boolean isNative(Element element) {
		return element.getModifiers().contains(Modifier.NATIVE) || element.getAnnotation(Native.class) != null;
	}
}
