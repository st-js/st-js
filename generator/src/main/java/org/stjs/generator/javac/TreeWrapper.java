package org.stjs.generator.javac;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.SyntheticType;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;

/**
 * 
 * this class is a wrapper around a {@link Tree} node to give you easier access to the most important methods of the
 * elements in the AST
 * 
 * @author acraciun
 */
public class TreeWrapper<T extends Tree, JS> {
	private final TreePath path;
	private final GenerationContext<JS> context;
	private final Element element;

	public TreeWrapper(@Nonnull TreePath path, @Nonnull GenerationContext<JS> context) {
		this.context = context;
		this.path = path;
		this.element = getElement(path.getLeaf());
	}

	private Element getElement(Tree tree) {
		return InternalUtils.symbol(tree);
	}

	@SuppressWarnings("unchecked")
	public T getTree() {
		return (T) path.getLeaf();
	}

	public TreePath getPath() {
		return path;
	}

	public GenerationContext<JS> getContext() {
		return context;
	}

	public boolean isFinal() {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.FINAL);
	}

	public boolean isStatic() {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.STATIC);
	}

	public boolean isSuper() {
		if (!(getTree() instanceof IdentifierTree)) {
			return false;
		}
		return GeneratorConstants.SUPER.equals(((IdentifierTree) getTree()).getName().toString());
	}

	// @SuppressWarnings("unchecked")
	// private <A extends Annotation> Class<A> getAnnotationFromClassLoader(Class<A> annotationType) {
	// try {
	// return (Class<A>) context.getBuiltProjectClassLoader().loadClass(annotationType.getName());
	// } catch (ClassNotFoundException e) {
	// throw new STJSRuntimeException("Cannot load the annotation type:" + annotationType
	// + ". This is maybe a ST-JS bug. Please report it to our website");
	// }
	// }

	private <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		A a = element.getAnnotation(annotationType);
		if (a != null) {
			return a;
		}
		PackageElement pack = ElementUtils.enclosingPackage(element);
		return pack == null ? null : pack.getAnnotation(annotationType);
	}

	public boolean isJavaScriptFunction() {
		return getAnnotation(JavascriptFunction.class) != null;
	}

	public boolean isGlobal() {
		return getAnnotation(GlobalScope.class) != null;
	}

	public String getNamespace() {
		Namespace ns = element.getAnnotation(Namespace.class);
		if (ns != null) {
			return ns.value();
		}
		return null;
	}

	public boolean isInnerType() {
		return element.getEnclosingElement().getKind() != ElementKind.PACKAGE;
	}

	public <C extends Tree> TreeWrapper<C, JS> child(C child) {
		return context.wrap(new TreePath(path, child));
	}

	@SuppressWarnings("deprecation")
	public boolean isSyntheticType() {
		return getAnnotation(SyntheticType.class) != null || getAnnotation(DataType.class) != null;
	}

	public boolean isNative() {
		return element.getModifiers().contains(Modifier.NATIVE) || element.getAnnotation(Native.class) != null;
	}

	public boolean isJavaScriptPrimitive() {
		TypeMirror type = element.asType();
		return TypesUtils.isPrimitive(type) || TypesUtils.isBoxedPrimitive(type) || TypesUtils.isString(type);
	}
}
