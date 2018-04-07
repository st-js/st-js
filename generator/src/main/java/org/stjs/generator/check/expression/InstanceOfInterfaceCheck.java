package org.stjs.generator.check.expression;

import com.sun.source.tree.InstanceOfTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Type;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import javax.lang.model.type.TypeMirror;

/**
 * In TypeScript, interfaces only exist at compilation time, at runtime they don't exist.
 * Though it's valid Java to test for interfaces, we ensure that isn't the case for JavaScript code.
 */
public class InstanceOfInterfaceCheck implements CheckContributor<InstanceOfTree> {

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, InstanceOfTree tree, GenerationContext<Void> context) {

		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));

		if (type instanceof Type.ClassType && ((Type.ClassType) type).isInterface()) {
			context.addError(tree, "You cannot call instanceof on an interface");
		}

		return null;
	}

}

