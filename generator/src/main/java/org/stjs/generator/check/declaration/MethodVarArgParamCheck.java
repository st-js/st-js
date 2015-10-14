package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.MemberWriters;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

public class MethodVarArgParamCheck implements CheckContributor<MethodTree> {

	private void checkVarArg(MethodTree tree, VariableTree param, GenerationContext<Void> context) {
		if (!param.getName().toString().equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			context.addError(param, "You can only have a vararg parameter that has to be called 'arguments'");

		}
		if (tree.getParameters().size() != 1) {
			context.addError(tree, "You can only have a vararg parameter that has to be called 'arguments'");
		}
	}

	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		TreeWrapper<Tree, Void> tw = context.getCurrentWrapper();
		if (MemberWriters.shouldSkip(tw)) {
			return null;
		}

		// Native methods can be safely ignored
		if (tw.isNative()) {
			return null;
		}

		for (VariableTree param : tree.getParameters()) {
			if (InternalUtils.isVarArg(param)) {
				checkVarArg(tree, param, context);
			}
		}
		return null;
	}

}
