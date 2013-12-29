package org.stjs.generator.utils;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.JavascriptClassGenerationException;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public final class JavaNodes {
	private static final String ANNOTATED_PACKAGE = "annotation.";

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

	private static <T extends Annotation> T getAnnotation(Element element, Class<T> annotationType) {
		T a = element.getAnnotation(annotationType);
		if (a != null) {
			return a;
		}
		PackageElement pack = ElementUtils.enclosingPackage(element);
		return pack == null ? null : pack.getAnnotation(annotationType);
	}

	public static boolean isJavaScriptFunction(Element element) {
		return getAnnotation(element, JavascriptFunction.class) != null;
	}

	public static boolean isGlobal(Element element) {
		return getAnnotation(element, GlobalScope.class) != null;
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

	// TODO add non-static cache
	// private static Map<String, String> cache = Maps.newHashMap();
	// private static final String NULL = new String();
	//
	// public static String getMethodTemplate(Elements elements, Element element) {
	// String key = ElementUtils.getQualifiedClassName(element) + "." + element.getSimpleName();
	//
	// String ret = cache.get(key);
	// if (ret != null) {
	// return ret == NULL ? null : ret;
	// }
	// ret = getMethodTemplate0(elements, element);
	// if (ret != null) {
	// cache.put(key, ret);
	// } else {
	// cache.put(key, NULL);
	// }
	// return ret;
	// }

	public static String getMethodTemplate(Elements elements, Element element) {

		Template t = element.getAnnotation(Template.class);
		if (t != null) {
			return t.value();
		}

		if (!(element instanceof ExecutableElement)) {
			// make sure we're only doing it for methods
			return null;
		}
		// give it a second chance (for classes in another jars or in the JDK, by using ...)
		t = getAnnotationInHelpers(elements, (ExecutableElement) element, Template.class);
		if (t != null) {
			return t.value();
		}
		return null;
	}

	private static String capitalize(String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 1) {
			return s.toUpperCase(Locale.getDefault());
		}
		return s.substring(0, 1).toUpperCase(Locale.getDefault()) + s.substring(1);
	}

	private static <T extends Annotation> T getAnnotationInHelpers(Elements elements, ExecutableElement methodElement, Class<T> annotationClass) {
		// 1. look for a class in the same package of the declaring class but with the name of the method (with the
		// 1st letter capitalized) attached
		// and the suffix "Annotated"
		String ownerClassName = ((TypeElement) methodElement.getEnclosingElement()).getQualifiedName().toString();
		T annotation = getAnnotationInHelperClass(elements, ANNOTATED_PACKAGE + ownerClassName
				+ capitalize(methodElement.getSimpleName().toString()), methodElement, annotationClass);
		if (annotation != null) {
			return annotation;
		}

		// 2. look for a class in the same package of the declaring class but with the suffix "Annotated"
		return getAnnotationInHelperClass(elements, ANNOTATED_PACKAGE + ownerClassName, methodElement, annotationClass);
	}

	private static <T extends Annotation> T getAnnotationInHelperClass(Elements elements, String helperClassName,
			ExecutableElement methodElement, Class<T> annotationClass) {
		TypeElement type = elements.getTypeElement(helperClassName);

		if (type == null) {
			return null;
		}

		// find a method with the same signature in the new class
		for (Element member : elements.getAllMembers(type)) {
			if (member instanceof ExecutableElement && sameSignature((ExecutableElement) member, methodElement)) {
				return member.getAnnotation(annotationClass);
			}
		}
		return null;
	}

	private static boolean sameSignature(ExecutableElement member, ExecutableElement methodElement) {
		// TODO Auto-generated method stub
		return member.getSimpleName().equals(methodElement.getSimpleName());
	}

	@SuppressWarnings("deprecation")
	public static boolean isSyntheticType(Element type) {
		return getAnnotation(type, SyntheticType.class) != null || getAnnotation(type, DataType.class) != null;
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
