package org.stjs.generator.check.declaration;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.javascript.annotation.ServerSide;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This check make sure that there is no single implementation of a method in a base class where a child of the base class
 * has overloaded methods of the same method name.
 */
public class MethodOverloadManyToSingleCheck implements CheckContributor<MethodTree> {
    @Override
    public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
        ExecutableElement methodElement = TreeUtils.elementFromDeclaration(tree);
        if (JavaNodes.isNative(methodElement)) {
            // no need to check the native ones - only the one with the body
            return null;
        }
        TreeWrapper<Tree, Void> tw = context.getCurrentWrapper();
        if (MemberWriters.shouldSkip(tw)) {
            return null;
        }

        if (ElementKind.CONSTRUCTOR.equals(methodElement.getKind())) {
            return null;
        }

        checkSuperClassOverloadedMethods(methodElement, context, tree);

        return null;
    }

    private void checkSuperClassOverloadedMethods(ExecutableElement methodElement,
                                                  GenerationContext<Void> context, MethodTree tree) {
        if (methodElement.getAnnotation(ServerSide.class) != null) {
            return;
        }

        Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes =
                mapExecutableElementsToResolvedName(context, ElementUtils.getAllMethodsFromSupertypes(ElementUtils.enclosingClass(methodElement)));

        checkAllMethodsWithTheSameNameHaveTheSameSignature(context, tree, methodElement, allMethodNamesFromSuperTypes);
        checkAllMethodsWithTheSameSignatureHaveTheSameName(context, tree, methodElement, allMethodNamesFromSuperTypes);

    }

    private void checkAllMethodsWithTheSameNameHaveTheSameSignature(GenerationContext<Void> context,
                                                                    MethodTree tree,
                                                                    ExecutableElement model,
                                                                    Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes) {
        String methodName = context.getNames().getMethodName(context, model);
        List<ExecutableElement> executableElements = allMethodNamesFromSuperTypes.get(methodName);

        if (executableElements == null) {
            return;
        }

        List<ExecutableElement> methodNameConflict = new ArrayList<>();
        String methodSignature = model.toString();
        for (ExecutableElement executableElement : executableElements) {
            if (!methodSignature.equals(executableElement.toString())) {
                methodNameConflict.add(executableElement);
            }
        }

        if (!methodNameConflict.isEmpty()) {
            context.addError(tree,
                    String.format(
                            "Method name conflict for: [%s.%s]. "
                                    + "Class hierarchy contains methods with the same name [%s] but with different signatures: %s",
                            model.getEnclosingElement().getSimpleName(),
                            model.toString(),
                            methodName,
                            buildOverridenMethodErrorMessage_sameNameButDifferentSignature(methodNameConflict)));
        }
    }

    private String buildOverridenMethodErrorMessage_sameNameButDifferentSignature(List<ExecutableElement> methodList) {
        StringBuilder sb = new StringBuilder();

        for (ExecutableElement executableElement : methodList) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append('\'');
            sb.append(executableElement.getEnclosingElement().getSimpleName());
            sb.append('.');
            sb.append(executableElement.toString());
            sb.append('\'');
        }

        return sb.toString();
    }

    private void checkAllMethodsWithTheSameSignatureHaveTheSameName(GenerationContext<Void> context,
                                                                    MethodTree tree,
                                                                    ExecutableElement model,
                                                                    Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes) {
        String methodName = context.getNames().getMethodName(context, model);

        List<ExecutableElement> methodNameConflict = new ArrayList<>();

        for (Map.Entry<String, List<ExecutableElement>> mapEntry : allMethodNamesFromSuperTypes.entrySet()) {
            String mappedMethodName = mapEntry.getKey();

            if (!methodName.equals(mappedMethodName)) {
                // Not the same name: search for a method with the same signature
                List<ExecutableElement> executableElements = mapEntry.getValue();

                for (ExecutableElement executableElement : executableElements) {
                    if (ElementUtils.sameSignature(context, model, executableElement)) {
                        methodNameConflict.add(executableElement);
                    }
                }
            }
        }

        if (!methodNameConflict.isEmpty()) {
            context.addError(tree,
                    String.format(
                            "Method name conflict for: [%s.%s --> %s]. "
                                    + "Class hierarchy contains methods with the same signature [%s] but with different resolved name: %s",
                            model.getEnclosingElement().getSimpleName(),
                            model.toString(),
                            methodName,
                            model.toString(),
                            buildOverridenMethodErrorMessage_sameSignatureButDifferentName(context, methodNameConflict)));
        }
    }

    private String buildOverridenMethodErrorMessage_sameSignatureButDifferentName(GenerationContext context, List<ExecutableElement> methodList) {
        StringBuilder sb = new StringBuilder();

        for (ExecutableElement executableElement : methodList) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append('[');

            sb.append('\'');
            sb.append(executableElement.getEnclosingElement().getSimpleName());
            sb.append('.');
            sb.append(executableElement.toString());
            sb.append('\'');

            sb.append(" --> ");

            sb.append(context.getNames().getMethodName(context, executableElement));

            sb.append(']');

        }

        return sb.toString();
    }

    private Map<String, List<ExecutableElement>> mapExecutableElementsToResolvedName(GenerationContext context, List<ExecutableElement> allMethodsFromSupertypes) {
        Map<String, List<ExecutableElement>> executableElementByResolvedName = new HashMap<>();

        for (ExecutableElement sameMethodFromParent : allMethodsFromSupertypes) {
            String methodName = context.getNames().getMethodName(context, sameMethodFromParent);

            // create entry in 'executableElementByResolvedName' if there is none
            List<ExecutableElement> matchingExecutableElementList = executableElementByResolvedName.get(methodName);
            if (matchingExecutableElementList == null) {
                matchingExecutableElementList = new ArrayList<>();
                executableElementByResolvedName.put(methodName, matchingExecutableElementList);
            }

            // add entry
            matchingExecutableElementList.add(sameMethodFromParent);
        }

        return executableElementByResolvedName;
    }

}
