package org.stjs.generator.utils;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

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
}
