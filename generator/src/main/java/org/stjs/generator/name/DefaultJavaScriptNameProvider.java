package org.stjs.generator.name;

import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;

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
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class DefaultJavaScriptNameProvider implements JavaScriptNameProvider {
	private static final String JAVA_LANG_PACKAGE = "java.lang.";
	private static final int JAVA_LANG_LENGTH = JAVA_LANG_PACKAGE.length();
	private static final Map<TypeKind, String> JS_ARRAY_TYPES = new HashMap<>();
	private final Map<String, DependencyType> resolvedRootTypes = new HashMap<String, DependencyType>();
	private final Map<TypeMirror, TypeInfo> resolvedTypes = new HashMap<TypeMirror, TypeInfo>();

	static {
		JS_ARRAY_TYPES.put(TypeKind.BOOLEAN, "Int8Array");
		JS_ARRAY_TYPES.put(TypeKind.BYTE, "Int8Array");
		JS_ARRAY_TYPES.put(TypeKind.SHORT, "Int16Array");
		JS_ARRAY_TYPES.put(TypeKind.CHAR, "Uint16Array");
		JS_ARRAY_TYPES.put(TypeKind.INT, "Int32Array");
		JS_ARRAY_TYPES.put(TypeKind.FLOAT, "Float32Array");
		JS_ARRAY_TYPES.put(TypeKind.DOUBLE, "Float64Array");
	}

	private class TypeInfo {
		private final String fullName;
		private final Element rootTypeElement;

		public TypeInfo(String fullName, Element rootTypeElement) {
			this.fullName = fullName;
			this.rootTypeElement = rootTypeElement;
		}

		public String getFullName() {
			return fullName;
		}

		public Element getRootTypeElement() {
			return rootTypeElement;
		}

	}

	private String addNameSpace(Element rootTypeElement, GenerationContext<?> context, String name) {
		String namespace = context.wrap(rootTypeElement).getNamespace();
		if (namespace.isEmpty()) {
			return name;
		}
		return namespace + "." + name;
	}

	/** {@inheritDoc} */
	@Override
	public String getTypeName(GenerationContext<?> context, TypeMirror type, DependencyType dependencyType) {
		TypeInfo typeInfo = resolvedTypes.get(type);
		if (typeInfo != null) {
			// make sure we have the strictest dep type
			addResolvedType(typeInfo.getRootTypeElement(), dependencyType);
			return typeInfo.getFullName();
		}

		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;
			String name = InternalUtils.getSimpleName(declaredType.asElement());
			Element rootTypeElement = declaredType.asElement();
			for (DeclaredType enclosingType = JavaNodes.getEnclosingType(declaredType); enclosingType != null; enclosingType = JavaNodes
					.getEnclosingType(enclosingType)) {
				rootTypeElement = enclosingType.asElement();
				name = InternalUtils.getSimpleName(rootTypeElement) + "." + name;
			}

			checkAllowedType(rootTypeElement, context);
			addResolvedType(rootTypeElement, dependencyType);

			String fullName = addNameSpace(rootTypeElement, context, name);
			resolvedTypes.put(type, new TypeInfo(fullName, rootTypeElement));
			return fullName;
		}
		if (type instanceof ArrayType) {
			return handleArrayType((ArrayType) type);
		}
		if (type instanceof WildcardType) {
			// ? extends Type1 super Type2
			// XXX what to return here !?
			return "Object";
		}
		return type.toString();
	}

	private static String handleArrayType(ArrayType atype) {
		TypeMirror componentType = atype.getComponentType();
		TypeKind kind = componentType.getKind();
		String jsType = JS_ARRAY_TYPES.get(kind);
		return jsType == null ? "Array" : jsType;
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

	private void addResolvedType(Element rootTypeElement, DependencyType depType) {
		String name = ElementUtils.getQualifiedClassName(rootTypeElement).toString();
		if (!name.startsWith("java.lang.")) {
			DependencyType prevDepType = resolvedRootTypes.get(name);
			if (prevDepType == null || depType.isStricter(prevDepType)) {
				resolvedRootTypes.put(name, depType);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getVariableName(GenerationContext<?> context, IdentifierTree treeNode, TreePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getMethodName(GenerationContext<?> context, MethodTree tree, TreePath path) {
		return tree.getName().toString();
	}

	/** {@inheritDoc} */
	@Override
	public String getMethodName(GenerationContext<?> context, MethodInvocationTree tree, TreePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getTypeName(GenerationContext<?> context, Element type, DependencyType dependencyType) {
		if (type == null) {
			return null;
		}
		return getTypeName(context, type.asType(), dependencyType);
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, DependencyType> getResolvedTypes() {
		return resolvedRootTypes;
	}

}
