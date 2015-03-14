package org.stjs.generator.check.expression;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;

/**
 * this check verifies that you don't call a method from the outer type as in javaScript this scope is not accessible.
 * @author acraciun
 */
public class MethodInvocationOuterScopeCheck implements CheckContributor<MethodInvocationTree> {
	private void checkScope(Element methodElement, MethodInvocationTree tree, GenerationContext<Void> context) {
		ClassTree enclosingClassTree = IdentifierAccessOuterScopeCheck.enclosingClassSkipAnonymousInitializer(context.getCurrentPath());

		TypeElement currentScopeClassElement = TreeUtils.elementFromDeclaration(enclosingClassTree);
		TypeElement methodOwnerElement = (TypeElement) methodElement.getEnclosingElement();
		if (IdentifierAccessOuterScopeCheck.isOuterType(context, methodOwnerElement, currentScopeClassElement)) {
			context.addError(tree, "In Javascript you cannot call methods or fields from the outer type. "
					+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}
	}

	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		Element methodElement = TreeUtils.elementFromUse(tree);

		if (JavaNodes.isStatic(methodElement)) {
			// only instance methods
			return null;
		}

		String name = MethodInvocationWriter.buildMethodName(tree);

		if (GeneratorConstants.THIS.equals(name) || GeneratorConstants.SUPER.equals(name)) {
			// this and super call are ok
			return null;
		}

		if (!(tree.getMethodSelect() instanceof IdentifierTree)) {
			// check for Outer.this check
			return null;
		}

		checkScope(methodElement, tree, context);
		return null;
	}
}
