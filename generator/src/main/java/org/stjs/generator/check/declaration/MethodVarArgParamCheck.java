package org.stjs.generator.check.declaration;

import javacutils.InternalUtils;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

public class MethodVarArgParamCheck implements VisitorContributor<MethodTree, Void, GenerationContext> {

	private void checkVarArg(MethodTree tree, VariableTree param, GenerationContext context) {
		if (!param.getName().toString().equals(GeneratorConstants.ARGUMENTS_PARAMETER)) {
			context.addError(param, "You can only have a vararg parameter that has to be called 'arguments'");

		}
		if (tree.getParameters().size() != 1) {
			context.addError(tree, "You can only have a vararg parameter that has to be called 'arguments'");
		}
	}

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, MethodTree tree, GenerationContext context, Void prev) {
		for (VariableTree param : tree.getParameters()) {
			if (InternalUtils.isVarArg(param)) {
				checkVarArg(tree, param, context);
			}
		}
		return null;
	}

}
