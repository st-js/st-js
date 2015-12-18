package org.stjs.generator.check.declaration;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import org.stjs.generator.AnnotationUtils;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.MemberWriters;
import org.stjs.javascript.annotation.JSOverloadName;
import org.stjs.javascript.annotation.ServerSide;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
        if (ElementUtils.hasAnOverloadedEquivalentMethod(methodElement, context.getElements())) {
            checkSuperClassOverloadedMethods(methodElement, typeElement, context, tree);
        }

        return null;
    }

    private void checkSuperClassOverloadedMethods(ExecutableElement methodElement, TypeElement typeElement,
                                                  GenerationContext<Void> context, MethodTree tree) {
        if (methodElement.getAnnotation(ServerSide.class) != null) {
            return;
        }

        List<ExecutableElement> sameMethodFromParents = ElementUtils.getSameMethodFromParents(methodElement);

        Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes = new HashMap<>();
        for (ExecutableElement sameMethodFromParent : sameMethodFromParents) {
            String methodName = context.getNames().getMethodName(context, sameMethodFromParent);

            List<ExecutableElement> matchingExecutableElements = allMethodNamesFromSuperTypes.get(methodName);
            if (matchingExecutableElements == null) {
                matchingExecutableElements = new ArrayList<>();
                allMethodNamesFromSuperTypes.put(methodName, matchingExecutableElements);
            }
            matchingExecutableElements.add(sameMethodFromParent);
        }

        if (allMethodNamesFromSuperTypes.size() >= 2) {
            context.addError(tree,
                    String.format(
                            "" +
                                    "Method name conflict for method with signature: '%s.%s']. " +
                                    "Class hierarchy contains different names for this method signature: %s",
                            methodElement.getEnclosingElement().getSimpleName(),
                            methodElement.toString(),
                            buildOverridenMethodErrorMessage(allMethodNamesFromSuperTypes)));
        }
    }

    private String buildOverridenMethodErrorMessage(Map<String, List<ExecutableElement>> allMethodNamesFromSuperTypes) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<ExecutableElement>> mapEntry : allMethodNamesFromSuperTypes.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("'");
            sb.append(mapEntry.getKey());
            sb.append("' --> ");
            sb.append(mapEntry.getValue().toString());
        }

        return sb.toString();
    }
}
