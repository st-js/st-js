package org.stjs.generator.check.declaration;

import com.sun.source.tree.MethodTree;
import com.sun.tools.javac.code.Type;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;

import javax.lang.model.type.TypeMirror;

/**
 * <p>MethodWithBodyInInterfaceCheck class.</p>
 *
 * @author sgoetz
 * @version $Id: $Id
 */
public class MethodWithBodyInInterfaceCheck implements CheckContributor<MethodTree> {

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, MethodTree tree, GenerationContext<Void> context) {
		TreeWrapper<MethodTree, Void> tw = context.getCurrentWrapper();

		// Generate interface methods differently
		TypeMirror type = context.getTrees().getTypeMirror(tw.getEnclosingType().getPath());
		if (type instanceof Type.ClassType && ((Type.ClassType) type).isInterface() && tree.getBody() != null) {
			context.addError(tree, "Methods in interfaces should not have a body. TypeScript doesn't support it.");
		}

		return null;
	}

}
