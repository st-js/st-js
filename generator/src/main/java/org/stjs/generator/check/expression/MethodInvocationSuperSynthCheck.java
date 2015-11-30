package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;

/**
 * this check verifies that you don't call a method from the super type if that is a synthetic type, as the actual
 * definition of this type does not exist.
 * 
 * @author acraciun
 * 
 */
public class MethodInvocationSuperSynthCheck implements CheckContributor<MethodInvocationTree> {

	private boolean checkSuperConstructor(MethodInvocationTree tree, GenerationContext<Void> context) {
		String name = MethodInvocationWriter.buildMethodName(tree, context);
		if (GeneratorConstants.SUPER.equals(name)) {
			if (!InternalUtils.isSyntheticConstructor(TreeUtils.enclosingOfKind(context.getCurrentPath(), Tree.Kind.METHOD))) {
				context.addError(tree, "You cannot call the super constructor if that belongs to a @SyntheticType");
			}
			return true;
		}
		return false;
	}

	private boolean checkSuperMethodCall(MethodInvocationTree tree, GenerationContext<Void> context) {
		if (!(tree.getMethodSelect() instanceof MemberSelectTree)) {
			// check for Outer.this check
			return true;
		}

		MemberSelectTree select = (MemberSelectTree) tree.getMethodSelect();
		if (GeneratorConstants.SUPER.equals(select.getExpression().toString())) {
			context.addError(tree, "You cannot call the super method if that belongs to a @SyntheticType");
			return true;
		}
		return false;
	}

	@Override
	public Void visit(CheckVisitor visitor, MethodInvocationTree tree, GenerationContext<Void> context) {
		TreeWrapper<MethodInvocationTree, Void> tw = context.getCurrentWrapper();
		if (!tw.getEnclosingType().isSyntheticType()) {
			return null;
		}

		if (checkSuperConstructor(tree, context)) {
			return null;
		}

		checkSuperMethodCall(tree, context);
		return null;
	}
}
