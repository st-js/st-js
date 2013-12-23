package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.writer.declaration.ClassWriter;

import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;

/**
 * this class checks that you don't use java arrays in the code (the only exception is the main method - but maybe this
 * only should also be forbidden). You should use {@link org.stjs.javascript.Array instead}.
 * 
 * @author acraciun
 */
public class ArrayTypeForbiddenCheck implements CheckContributor<ArrayTypeTree> {

	@Override
	public Void visit(CheckVisitor visitor, ArrayTypeTree tree, GenerationContext<Void> context) {
		if (!argOfMainMethod(context) && !isVarArg(context)) {
			context.addError(tree, "You cannot use Java arrays because they are incompatible with Javascript arrays. "
					+ "Use org.stjs.javascript.Array<T> instead. "
					+ "You can use also the method org.stjs.javascript.Global.$castArray to convert an "
					+ "existent Java array to the corresponding Array type." + "The only exception is void main(String[] args).");
		}
		return null;
	}

	private boolean isVarArg(GenerationContext<Void> context) {
		TreePath path = context.getCurrentPath();
		if (!(path.getParentPath().getLeaf() instanceof VariableTree)) {
			return false;
		}
		return InternalUtils.isVarArg(path.getParentPath().getLeaf());
	}

	private boolean argOfMainMethod(GenerationContext<Void> context) {
		TreePath path = context.getCurrentPath();
		if (!(path.getParentPath().getLeaf() instanceof VariableTree)) {
			return false;
		}
		if (!(path.getParentPath().getParentPath().getLeaf() instanceof MethodTree)) {
			return false;
		}
		MethodTree method = (MethodTree) path.getParentPath().getParentPath().getLeaf();
		if (!(method.getParameters().size() == 1 && method.getParameters().get(0) == path.getParentPath().getLeaf())) {
			// make sure we reference the first parameter
			return false;
		}
		return ClassWriter.isMainMethod(method);
	}

}
