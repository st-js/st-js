package org.stjs.generator.check.expression;

import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.InstanceOfTree;
import com.sun.source.util.TreePath;

/**
 * In TypeScript, interfaces only exist at compilation time, at runtime they don't exist.
 * Though it's valid Java to test for interfaces, we ensure that isn't the case for JavaScript code.
 */
public class InstanceOfPrimitiveCheck implements CheckContributor<InstanceOfTree> {

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, InstanceOfTree tree, GenerationContext<Void> context) {

		TypeMirror type = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getType()));

		if (JavaNodes.isJavaScriptPrimitive(type)) {
			context.addError(tree, "Using 'instanceof' on primitives does not work as you would expect, prefer using 'typeof' instead.");
		}

		return null;
	}

}

