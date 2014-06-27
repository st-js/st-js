package org.stjs.generator.name;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;

/**
 * This class implements the naming strategy transforming Java element names in JavaScript names.
 * @author acraciun
 */
public class DefaultJavaScriptNameProvider implements JavaScriptNameProvider {
	private static final String JAVA_LANG_PACKAGE = "java.lang.";
	private static final int JAVA_LANG_LENGTH = JAVA_LANG_PACKAGE.length();

	private final Set<String> resolvedRootTypes = new HashSet<String>();
	private final Map<TypeMirror, String> resolvedTypes = new HashMap<TypeMirror, String>();

	@Override
	public String getTypeName(GenerationContext<?> context, TypeMirror type) {
		String fullName = resolvedTypes.get(type);
		if (fullName != null) {
			return fullName;
		}

		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;
			String name = InternalUtils.getSimpleName(declaredType.asElement());
			Element rootTypeElement = declaredType.asElement();
			for (DeclaredType enclosingType = JavaNodes.getEnclosingType(declaredType); enclosingType != null; enclosingType =
					JavaNodes.getEnclosingType(enclosingType)) {
				rootTypeElement = enclosingType.asElement();
				name = InternalUtils.getSimpleName(rootTypeElement) + "." + name;
			}

			checkAllowedType(rootTypeElement, context);
			addResolvedType(rootTypeElement);

			String namespace = JavaNodes.getNamespace(rootTypeElement);
			fullName = (namespace == null ? "" : namespace + ".") + name;
			resolvedTypes.put(type, fullName);
			return fullName;
		}
		return type.toString();
	}

	private void typeNotAllowedException(GenerationContext<?> context, String name) {
		context.addError(context.getCurrentPath().getLeaf(), "The usage of the class " + name
				+ " is not allowed. If it's one of your own bridge types, "
				+ "please add the annotation @STJSBridge to the class or to its package.");
	}

	private boolean isJavaLangClassAllowed(GenerationContext<?> context, String name) {
		GeneratorConfiguration configuration = context.getConfiguration();
		if (name.startsWith(JAVA_LANG_PACKAGE) && configuration.getAllowedJavaLangClasses().contains(name.substring(JAVA_LANG_LENGTH))) {
			return true;
		}

		return false;
	}

	private boolean isPackageAllowed(GenerationContext<?> context, String name) {
		if (name.startsWith(JAVA_LANG_PACKAGE)) {
			return false;
		}
		GeneratorConfiguration configuration = context.getConfiguration();
		for (String packageName : configuration.getAllowedPackages()) {
			if (name.startsWith(packageName)) {
				return true;
			}
		}
		return false;
	}

	private boolean isBridge(GenerationContext<?> context, String name) {
		if (name.startsWith(JAVA_LANG_PACKAGE)) {
			return false;
		}

		return ClassUtils.isBridge(context.getBuiltProjectClassLoader(), ClassUtils.getClazz(context.getBuiltProjectClassLoader(), name));
	}

	private void checkAllowedType(Element rootTypeElement, GenerationContext<?> context) {
		String name = ElementUtils.getQualifiedClassName(rootTypeElement).toString();
		if (name.isEmpty()) {
			return;
		}
		if (isJavaLangClassAllowed(context, name)) {
			return;
		}

		if (isImportedStjsClass(context, name)) {
			return;
		}

		if (isPackageAllowed(context, name)) {
			return;
		}

		// ClassUtils.isBridge accepts all java.lang classes, that are actually not allowed
		if (isBridge(context, name)) {
			return;
		}

		typeNotAllowedException(context, name);
	}

	private boolean isImportedStjsClass(GenerationContext<?> context, String className) {
		String stjsPropertiesName = ClassUtils.getPropertiesFileName(className);
		return context.getBuiltProjectClassLoader().getResource(stjsPropertiesName) != null;
	}

	private void addResolvedType(Element rootTypeElement) {
		String name = ElementUtils.getQualifiedClassName(rootTypeElement).toString();
		if (!name.startsWith("java.lang.")) {
			resolvedRootTypes.add(name);
		}
	}

	@Override
	public String getVariableName(GenerationContext<?> context, IdentifierTree treeNode, TreePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMethodName(GenerationContext<?> context, MethodTree tree, TreePath path) {
		return tree.getName().toString();
	}

	@Override
	public String getMethodName(GenerationContext<?> context, MethodInvocationTree tree, TreePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTypeName(GenerationContext<?> context, Element type) {
		return getTypeName(context, type.asType());
	}

	@Override
	public Collection<String> getResolvedTypes() {
		return resolvedRootTypes;
	}

}
