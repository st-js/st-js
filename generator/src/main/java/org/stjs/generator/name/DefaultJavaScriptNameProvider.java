package org.stjs.generator.name;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public String transformMethodCallToFieldName(GenerationContext<?> context, MethodInvocationTree tree) {
        String methodName = getMethodName(context, tree);

        if (methodName.startsWith(GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR)) {
            methodName = methodName.substring(1, methodName.length());
        }

        int idx = methodName.indexOf(GeneratorConstants.AUTO_GENERATED_ELEMENT_SEPARATOR);
        if (idx > 0) {
            methodName = methodName.substring(0, idx);
        }

        return methodName;
    }

    @Override
    public String getMethodName(GenerationContext<?> context, MethodTree methodTree) {
        return getMethodName(context,
                TreeUtils.elementFromDeclaration(methodTree));
    }

    @Override
    public String getMethodName(GenerationContext<?> context, MethodInvocationTree tree) {
        ExecutableElement executableElement = TreeUtils.elementFromUse(tree);

        // Ignore super() calls, these are never going to be prefixed
        if (ElementUtils.isConstructor(executableElement)) {
            return GeneratorConstants.SUPER;
        }

        return getMethodName(context, executableElement);
    }

    @Override
    public String getMethodName(GenerationContext<?> context, ExecutableElement methodElement) {

        String annotationValue = AnnotationUtils.JSOverloadName.getAnnotationValue(context, methodElement);
        if (annotationValue != null) {
            return decorateNonPublicMembers(methodElement, annotationValue);
        }

        List<ExecutableElement> sameMethodsFromParents =
                ElementUtils.getSameMethodsFromSupertypes(context, ElementUtils.enclosingClass(methodElement), methodElement);

        if (sameMethodsFromParents.isEmpty()) {
            return getMethodNameFromClass(context, methodElement);
        } else {
            // found same method in some of parent class.
            // Ensure all parent classes returns the same name for the method
            Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes = new HashMap<>();
            for (ExecutableElement sameMethodFromParent : sameMethodsFromParents) {
                String methodName = getMethodName(context, sameMethodFromParent);

                List<ExecutableElement> matchingExecutableElements = allMethodNamesFromSuperTypes.get(methodName);
                if (matchingExecutableElements == null) {
                    matchingExecutableElements = new ArrayList<>();
                    allMethodNamesFromSuperTypes.put(methodName, matchingExecutableElements);
                }
                matchingExecutableElements.add(sameMethodFromParent);
            }

            if (allMethodNamesFromSuperTypes.size() >= 2) {
                context.addError(context.getCurrentPath().getCompilationUnit(),
                        String.format(
                                "Method name conflict for method with signature: [%s.%s]. "
                                        + "Parent class hierarchy uses different method names fro the same method type erasure: %s",
                                methodElement.getEnclosingElement().getSimpleName(),
                                methodElement.toString(),
                                buildOverridenMethodErrorMessage(allMethodNamesFromSuperTypes)));
            }

            // return first
            return allMethodNamesFromSuperTypes.keySet().iterator().next();
        }
    }

    private String buildOverridenMethodErrorMessage(Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<ExecutableElement>> mapEntry : allMethodNamesFromSuperTypes.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append('\'');
            sb.append(mapEntry.getKey());
            sb.append("' --> ");

            List<ExecutableElement> value = mapEntry.getValue();

            sb.append('[');
            int sbLengthBeforeLoop = sb.length();
            for (ExecutableElement executableElement : value) {
                if (sb.length() != sbLengthBeforeLoop) {
                    sb.append(", ");
                }

                sb.append(executableElement.getEnclosingElement().getSimpleName());
                sb.append('.');
                sb.append(executableElement.toString());
            }
            sb.append(']');

            sb.append(mapEntry.getValue().toString());
        }

        return sb.toString();
    }

    private String getMethodNameFromClass(GenerationContext context, ExecutableElement methodElement) {
        List<ExecutableElement> methodsInClassWithSameName = findAllMethodsInClassWithSameName(methodElement);
        if (methodsInClassWithSameName.isEmpty() && !ElementUtils.isConstructor(methodElement)) {
            throw new AssertionError("Should have matched at least the current method.");
        }
        methodsInClassWithSameName = filterOutNativeMethods(methodsInClassWithSameName);
        methodsInClassWithSameName = filterOutMethodsWithSameSignature(methodsInClassWithSameName, methodElement);

        String methodName = methodElement.getSimpleName().toString();
        if (!methodsInClassWithSameName.isEmpty()) {
            // generate method name with argument types
            List<? extends VariableElement> params = methodElement.getParameters();
            methodName = InternalUtils.generateOverloadedMethodName(context, methodName, params);
        }

        methodName = decorateNonPublicMembers(methodElement, methodName);

        return methodName;
    }

    private List<ExecutableElement> filterOutMethodsWithSameSignature(
            List<ExecutableElement> methodsInClassWithSameName, ExecutableElement model) {
        List<ExecutableElement> filteredList = new ArrayList<>();

        String modelSignature = model.toString();
        for (ExecutableElement executableElement : methodsInClassWithSameName) {
            if (!modelSignature.equals(executableElement.toString())) {
                filteredList.add(executableElement);
            }
        }

        return filteredList;

    }

    private List<ExecutableElement> filterOutNativeMethods(List<ExecutableElement> methodsInClassWithSameName) {
        List<ExecutableElement> filteredList = new ArrayList<>();

        for (ExecutableElement executableElement : methodsInClassWithSameName) {
            if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                filteredList.add(executableElement);
            }
        }

        return filteredList;
    }

    private String decorateNonPublicMembers(Element element, String memberName) {
        String decoratedMemberName = memberName;

        boolean isPublic = element.getModifiers().contains(Modifier.PUBLIC)
                || element.getEnclosingElement().getKind() == ElementKind.INTERFACE;

        if (!isPublic) {
            decoratedMemberName = GeneratorConstants.NON_PUBLIC_METHODS_AND_FIELDS_PREFIX + memberName;
        }

        return decoratedMemberName;
    }

    private List<ExecutableElement> findAllMethodsInClassWithSameName(ExecutableElement methodElement) {
        String methodSimpleName = methodElement.getSimpleName().toString();

        List<ExecutableElement> methodsInClassWithSameName = new ArrayList<>();
        List<ExecutableElement> allMethodsInClass = ElementUtils.getAllMethodsIn(ElementUtils.enclosingClass(methodElement));
        for (ExecutableElement method : allMethodsInClass) {
            if (method.getSimpleName().toString().equals(methodSimpleName)) {
                methodsInClassWithSameName.add(method);
            }
        }

        return methodsInClassWithSameName;
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

}
