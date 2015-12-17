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
import org.stjs.javascript.annotation.JSOverloadName;
import org.stjs.javascript.annotation.ServerSide;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

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

        TypeMirror superClass = typeElement.getSuperclass();
        TypeElement superClassElement = (TypeElement) ((DeclaredType) superClass).asElement();
        List<? extends Element> allMembers = context.getElements().getAllMembers(superClassElement);

        if (ElementUtils.hasAnOverloadedEquivalentMethod(methodElement, allMembers)
                && (superClassElement.getAnnotation(JSOverloadName.class) == null)) {
            context.addError(tree, "There is a method in a parent having the same name [" + tree.getName() + "]"
                    + " than your overloaded method but your class only implement one, which leads to not being overloaded."
                    + " This will lead to a clash in the invocation of the method. "
                    + "Use the annotation '@JSOverloadName(\"newMethodNameHere\")' to provide another method name at generation.");
        }
    }
}
