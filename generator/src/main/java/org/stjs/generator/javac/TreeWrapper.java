package org.stjs.generator.javac;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.NamespaceUtil;
import org.stjs.generator.name.DependencyType;
import org.stjs.javascript.annotation.DataType;
import org.stjs.javascript.annotation.GlobalScope;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.Native;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.ServerSide;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;

/**
 * this class is a wrapper around a {@link com.sun.source.tree.Tree} node to give you easier access to the most important methods of the
 * elements in the AST
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class TreeWrapper<T extends Tree, JS> {
	private final TreePath path;
	private final GenerationContext<JS> context;
	private final Element element;
	private String jsNamespace;

	/**
	 * <p>Constructor for TreeWrapper.</p>
	 *
	 * @param path a {@link com.sun.source.util.TreePath} object.
	 * @param context a {@link org.stjs.generator.GenerationContext} object.
	 */
	public TreeWrapper(@Nonnull TreePath path, @Nonnull GenerationContext<JS> context) {
		this.context = context;
		this.path = path;
		this.element = getElement(path.getLeaf());
	}

	/**
	 * <p>Constructor for TreeWrapper.</p>
	 *
	 * @param element a {@link javax.lang.model.element.Element} object.
	 * @param path a {@link com.sun.source.util.TreePath} object.
	 * @param context a {@link org.stjs.generator.GenerationContext} object.
	 */
	public TreeWrapper(@Nonnull Element element, @Nonnull TreePath path, @Nonnull GenerationContext<JS> context) {
		this.context = context;
		this.path = path;
		this.element = element;
	}

	private Element getElement(Tree tree) {
		return InternalUtils.symbol(tree);
	}

	/**
	 * <p>getTree.</p>
	 *
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public T getTree() {
		return (T) path.getLeaf();
	}

	/**
	 * <p>Getter for the field <code>path</code>.</p>
	 *
	 * @return a {@link com.sun.source.util.TreePath} object.
	 */
	public TreePath getPath() {
		return path;
	}

	/**
	 * <p>Getter for the field <code>context</code>.</p>
	 *
	 * @return a {@link org.stjs.generator.GenerationContext} object.
	 */
	public GenerationContext<JS> getContext() {
		return context;
	}

	/**
	 * <p>isFinal.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFinal() {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.FINAL);
	}

	/**
	 * <p>isStatic.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isStatic() {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.STATIC);
	}

	/**
	 * <p>isPrivate.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPrivate() {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.PRIVATE);
	}

	/**
	 * <p>isAbstract.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isAbstract() {
		Set<Modifier> modifiers = element.getModifiers();
		return modifiers.contains(Modifier.ABSTRACT);
	}

	/**
	 * <p>isSuper.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSuper() {
		if (!(getTree() instanceof IdentifierTree)) {
			return false;
		}
		return GeneratorConstants.SUPER.equals(((IdentifierTree) getTree()).getName().toString());
	}

	/**
	 * <p>isPrimitiveType.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isPrimitiveType() {
		return TypesUtils.isPrimitive(element.asType());
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

	/**
	 * <p>isJavaScriptFunction.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isJavaScriptFunction() {
		return getAnnotation(JavascriptFunction.class) != null;
	}

	/**
	 * <p>isGlobal.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isGlobal() {
		return getAnnotation(GlobalScope.class) != null;
	}

	public boolean isBridge() {
		return getAnnotation(STJSBridge.class) != null;
	}

	/**
	 * <p>getNamespace.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNamespace() {
		if (jsNamespace == null) {
			jsNamespace = doGetNamespace();
		}
		return jsNamespace;
	}

	private String doGetNamespace() {
		// Only bridges can have namespaces
		if (!isBridge()) {
			return "";
		}

		// Check if we can find the namespace directly at the source level
		String ns = getNamespaceFromElement();
		if (ns != null) {
			return ns;
		}

		// Not found, loading it from the classpath or by reflection
		ns = getNamespaceByReflection();
		if (ns != null) {
			return ns;
		}

		// No namespace
		return "";
	}

	private String getNamespaceByReflection() {
		String ns = null;
		if (element instanceof PackageElement) {
			ns = getPackageNamespace(((PackageElement) element).getQualifiedName().toString());

		} else if (element instanceof TypeElement) {
			ns = getTypeElementNamespace();
		}
		return ns;
	}

	private String getNamespaceFromElement() {
		Namespace nsAnnotation = element.getAnnotation(Namespace.class);
		if (nsAnnotation != null) {
			return nsAnnotation.value();
		}
		return null;
	}

	private String getTypeElementNamespace() {
		// inner types inherit their namespace from their topmost containing class, so lets find it
		TypeElement root = (TypeElement) element;
		while (root.getEnclosingElement() instanceof TypeElement) {
			root = (TypeElement) root.getEnclosingElement();
		}

		// if the enclosing type of the topmost containing class is not package,
		// then we are actually in an anonymous class. Those cannot have namespaces, and cannot be
		// looked up because they have no name either.
		if (root.getEnclosingElement().getKind() == ElementKind.PACKAGE) {
			return getTypeNamespace(root.getQualifiedName().toString());
		}
		return null;
	}

	private String getPackageNamespace(String qualifiedName) {
		return NamespaceUtil.resolvePackageNamespace(qualifiedName, context.getBuiltProjectClassLoader());
	}

	private String getTypeNamespace(String qualifiedName) {
		return NamespaceUtil.resolveNamespace(qualifiedName, context.getBuiltProjectClassLoader());
	}

	/**
	 * <p>isInnerType.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isInnerType() {
		return element.getEnclosingElement().getKind() != ElementKind.PACKAGE;
	}

	/**
	 * <p>child.</p>
	 *
	 * @param child a C object.
	 * @return a {@link org.stjs.generator.javac.TreeWrapper} object.
	 */
	public <C extends Tree> TreeWrapper<C, JS> child(C child) {
		return context.wrap(new TreePath(path, child));
	}

	/**
	 * <p>parent.</p>
	 *
	 * @return a {@link org.stjs.generator.javac.TreeWrapper} object.
	 */
	public <P extends Tree> TreeWrapper<P, JS> parent() {
		if (path.getParentPath() != null) {
			return context.wrap(path.getParentPath());
		}
		return null;
	}

	/**
	 * <p>isSyntheticType.</p>
	 *
	 * @return a boolean.
	 */
	@SuppressWarnings("deprecation")
	public boolean isSyntheticType() {
		return getAnnotation(SyntheticType.class) != null || getAnnotation(DataType.class) != null;
	}

	/**
	 * <p>isNative.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isNative() {
		return element.getModifiers().contains(Modifier.NATIVE) || element.getAnnotation(Native.class) != null;
	}

	/**
	 * <p>isServerSide.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isServerSide() {
		return element.getAnnotation(ServerSide.class) != null;
	}

	/**
	 * <p>isJavaScriptPrimitive.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isJavaScriptPrimitive() {
		TypeMirror type = element.asType();
		return TypesUtils.isPrimitive(type) || TypesUtils.isBoxedPrimitive(type) || TypesUtils.isString(type);
	}

	/**
	 * <p>Getter for the field <code>element</code>.</p>
	 *
	 * @return a {@link javax.lang.model.element.Element} object.
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * <p>getEnclosingType.</p>
	 *
	 * @return a {@link org.stjs.generator.javac.TreeWrapper} object.
	 */
	public TreeWrapper<ClassTree, JS> getEnclosingType() {
		return context.wrap(element.getEnclosingElement());
	}

	/**
	 * <p>getCurrentType.</p>
	 *
	 * @return a {@link org.stjs.generator.javac.TreeWrapper} object.
	 */
	public TreeWrapper<ClassTree, JS> getCurrentType() {
		TreePath classPath = TreeUtils.enclosingPathOfType(path, ClassTree.class);
		if (classPath == null) {
			return null;
		}

		return context.wrap(classPath);
	}

	/**
	 * <p>getTypeName.</p>
	 *
	 * @return the type's name if the tree belongs to a type, undefined otherwise
	 * @param depType a {@link org.stjs.generator.name.DependencyType} object.
	 */
	public String getTypeName(DependencyType depType) {
		return context.getNames().getTypeName(context, element, depType);
	}

	/**
	 * <p>addError.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public void addError(String message) {
		context.addError(getTree(), message);
	}

	/**
	 * <p>getMethodTemplate.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMethodTemplate() {
		return stripParameters(getTemplateValue());
	}

	/**
	 * <p>getMethodTemplateParameters.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getMethodTemplateParameters() {
		return getTemplateParameters(getTemplateValue());
	}

	/**
	 * <p>getFieldTemplate.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldTemplate() {
		if (element == null || element.getKind() != ElementKind.FIELD) {
			return null;
		}
		return stripParameters(getTemplateValue());
	}

	/**
	 * <p>getFieldTemplateParameters.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getFieldTemplateParameters() {
		if (element == null || element.getKind() != ElementKind.FIELD) {
			return new String[ 0 ];
		}
		return getTemplateParameters(getTemplateValue());
	}

	private String stripParameters(String templateValue) {
		if (templateValue == null) {
			return null;
		}
		int paramListIndex = templateValue.indexOf('(');
		if (paramListIndex >= 0) {
			return templateValue.substring(0, paramListIndex).trim();
		}
		return templateValue;
	}

	private String[] getTemplateParameters(String templateValue) {
		if (templateValue == null) {
			return new String[ 0 ];
		}
		int paramIndex = templateValue.indexOf('(');
		if (paramIndex < 0) {
			return new String[ 0 ];
		}

		String params = templateValue.trim().substring(paramIndex + 1, templateValue.length() - 1);
		return params.split("\\s*,\\s*");
	}

	private String getTemplateValue() {
		Template tpl = context.getAnnotation(element, Template.class);
		if (tpl == null) {
			return null;
		}
		return tpl.value();
	}
}
