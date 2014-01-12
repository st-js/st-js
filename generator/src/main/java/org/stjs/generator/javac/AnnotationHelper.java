package org.stjs.generator.javac;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public final class AnnotationHelper {
	private static final String ANNOTATED_PACKAGE = "annotation.";

	private AnnotationHelper() {
		// private
	}

	public static <T extends Annotation> T getAnnotation(Elements elements, Element element, Class<T> annotationType) {
		if (!(element instanceof ExecutableElement)) {
			// make sure we're only doing it for methods
			return null;
		}

		T t = element.getAnnotation(annotationType);
		if (t != null) {
			return t;
		}
		// look into the definition in the super class
		List<ExecutableElement> similar = ElementUtils.getSameMethodFromParents((ExecutableElement) element);
		for (ExecutableElement method : similar) {
			t = method.getAnnotation(annotationType);
			if (t != null) {
				return t;
			}
		}
		// give it a second chance (for classes in another jars or in the JDK, by using ...)
		t = getAnnotationInHelpers(elements, (ExecutableElement) element, annotationType);
		return t;
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
}
