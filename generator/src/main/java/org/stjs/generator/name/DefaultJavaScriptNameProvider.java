package org.stjs.generator.name;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Symbol;
import org.stjs.generator.AnnotationUtils;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.ClassUtils;
import org.stjs.generator.utils.JavaNodes;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the naming strategy transforming Java element names in JavaScript names.
 *
 * @author acraciun
 */
public class DefaultJavaScriptNameProvider implements JavaScriptNameProvider {
	private static final String JAVA_LANG_PACKAGE = "java.lang.";
	private static final int JAVA_LANG_LENGTH = JAVA_LANG_PACKAGE.length();

	private final Map<String, DependencyType> resolvedRootTypes = new HashMap<String, DependencyType>();
	private final Map<TypeMirror, TypeInfo> resolvedTypes = new HashMap<TypeMirror, TypeInfo>();

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
		if (type instanceof WildcardType) {
			// ? extends Type1 super Type2
			// XXX what to return here !?
			return "Object";
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

	private void addResolvedType(Element rootTypeElement, DependencyType depType) {
		String name = ElementUtils.getQualifiedClassName(rootTypeElement).toString();
		if (!name.startsWith("java.lang.")) {
			DependencyType prevDepType = resolvedRootTypes.get(name);
			if (prevDepType == null || depType.isStricter(prevDepType)) {
				resolvedRootTypes.put(name, depType);
			}
		}
	}

	public String getFieldName(GenerationContext<?> context, MethodInvocationTree tree) {
		String name = context.getNames().getMethodName(context, tree);
		int start = name.startsWith("$") ? 1 : 0;
		return name.substring(start);
	}

	@Override
	public String getMethodName(GenerationContext<?> context, MethodTree tree) {
		Symbol.MethodSymbol element = (Symbol.MethodSymbol) context.getCurrentWrapper().getElement();
		String methodName = element.getSimpleName().toString();

		if (AnnotationUtils.JSOverloadName.isPresent(element)
				|| ElementUtils.hasAnOverloadedEquivalentMethod(TreeUtils.elementFromDeclaration(tree), context.getElements())) {
			methodName = AnnotationUtils.JSOverloadName.decorate(element);
		}

		if (!JavaNodes.isPublic(tree) && !isFromInterface(context)) {
			return GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + methodName;
		}

		return methodName;
	}


	private boolean isFromInterface(GenerationContext<?> context) {
		return ElementKind.INTERFACE.equals(context.getCurrentWrapper().getEnclosingType().getElement().getKind());
	}

	@Override
	public String getMethodName(GenerationContext<?> context, MethodInvocationTree tree) {
		ExpressionTree select = tree.getMethodSelect();

		if (select instanceof IdentifierTree) {
			// simple call: method(args)
			return buildMethodNameForIdentifierTree(tree, context, (IdentifierTree) select);
		} else if (select instanceof MemberSelectTree) {
			// calls with target: target.method(args)
			return buildMethodNameForMemberSelectTree(context, (MemberSelectTree) select);
		}
		throw context.addError(tree, "Unsupported tree type during buildMethodName.");
	}

	@Override
	public String getTypeName(GenerationContext<?> context, Element type, DependencyType dependencyType) {
		if (type == null) {
			return null;
		}
		return getTypeName(context, type.asType(), dependencyType);
	}

	@Override
	public Map<String, DependencyType> getResolvedTypes() {
		return resolvedRootTypes;
	}

	private <JS> String buildMethodNameForIdentifierTree(MethodInvocationTree tree, GenerationContext<JS> context, IdentifierTree select) {
		String methodName = select.getName().toString();

		// Ignore super() calls, these are never going to be prefixed
		if (GeneratorConstants.SUPER.equals(methodName)) {
			return methodName;
		}

		Symbol symbol = (Symbol.MethodSymbol) InternalUtils.symbol(tree);
		ExecutableElement methodElement = TreeUtils.getMethod(symbol);

		if (methodElement != null
				&& (AnnotationUtils.JSOverloadName.isPresent(methodElement)
				|| hasAnOverloadedMethod(context, methodElement))) {
			methodName = AnnotationUtils.JSOverloadName.decorate((Symbol.MethodSymbol) methodElement);
		}
		return prefixNonPublicMethods(methodName, symbol);
	}

	private <JS> String buildMethodNameForMemberSelectTree(GenerationContext<JS> context, MemberSelectTree memberSelect) {
		String methodName = memberSelect.getIdentifier().toString();
		Symbol symbol = (Symbol) InternalUtils.symbol(memberSelect);

		if (symbol != null && TreeUtils.isFieldAccess(memberSelect.getExpression())
				&& (symbol.getKind() == ElementKind.FIELD || symbol.getKind() == ElementKind.METHOD)) {
			return prefixNonPublicMethods(methodName, symbol);
		}

		ExecutableElement methodElement = TreeUtils.getMethod(symbol);
		if (hasAnOverloadedMethod(context, methodElement)) {
			return AnnotationUtils.JSOverloadName.decorate((Symbol.MethodSymbol) methodElement);
		}
		return methodName;
	}

	private <JS> boolean hasAnOverloadedMethod(GenerationContext<JS> context, ExecutableElement methodElement) {
		if (context == null) {
			return false;
		}
		return ElementUtils.hasAnOverloadedEquivalentMethod(methodElement, context.getElements());
	}

	private String prefixNonPublicMethods(String methodName, Symbol element) {
		if (element != null && element.getModifiers().contains(Modifier.PUBLIC)) {
			return methodName;
		} else {
			return GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + methodName;
		}
	}

}
