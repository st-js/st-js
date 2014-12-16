package org.stjs.generator.plugin.java8.check.expression;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreePath;

public class MethodAccessFromLambdaCheck implements CheckContributor<MethodInvocationTree> {
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

		TreePath enclosingLambdaPath = TreeUtils.enclosingPathOfType(context.getCurrentPath(), LambdaExpressionTree.class);

		if (enclosingLambdaPath != null) {
			context.addError(tree, "In Javascript you cannot access a method from the outer type. "
					+ "You should define a variable var that=this outside your lamda expression and use the method of this object. The method: "
					+ tree);
		}

		TreePath enclosingMethodReferencePath = TreeUtils.enclosingPathOfType(context.getCurrentPath(), MemberReferenceTree.class);
		if (enclosingMethodReferencePath != null) {
			context.addError(
					tree,
					"In Javascript you cannot access a method from the outer type. "
							+ "You should define a variable var that=this outside your member reference expression and use the method of this object. The method: "
							+ tree);
		}

		return null;
	}
}
