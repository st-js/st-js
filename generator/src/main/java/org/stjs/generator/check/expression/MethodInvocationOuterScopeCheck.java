package org.stjs.generator.check.expression;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.utils.Scopes;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import javax.lang.model.element.Element;

/**
 * this check verifies that you don't call a method from the outer type as in javaScript this scope is not accessible.
 * @author acraciun
 */
public class MethodInvocationOuterScopeCheck implements CheckContributor<MethodInvocationTree> {
    @Override
    public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
        Element methodElement = TreeUtils.elementFromUse(tree);

        if (JavaNodes.isStatic(methodElement)) {
            // only instance methods
            return null;
        }

        String name = MethodInvocationWriter.buildMethodName(tree, context);
        if (GeneratorConstants.THIS.equals(name) || GeneratorConstants.SUPER.equals(name)) {
            // this and super call are ok
            return null;
        }

        if (!(tree.getMethodSelect() instanceof IdentifierTree)) {
            // check for Outer.this check
            return null;
        }

        if (isInvokedFromAnonymousClass(context)) {
            return null;
        }

        checkScope(tree, context, methodElement);

        return null;
    }

    private void checkScope(MethodInvocationTree tree, GenerationContext<Void> context, Element methodElement) {
        if (Scopes.isInvokedElementFromOuterType(methodElement, context)) {
            context.addError(tree, "In Javascript you cannot call methods or fields from the outer type. "
                    + "You should define a variable var that=this outside your function definition and call the methods on this object");
        }
    }

    private boolean isInvokedFromAnonymousClass(GenerationContext<Void> context) {
        ClassTree enclosingClass = Scopes.findEnclosingClassSkipAnonymousInitializer(context.getCurrentPath());
        if (enclosingClass != null && enclosingClass.getSimpleName().toString().isEmpty()) {
            return true;
        }
        return false;
    }
}
