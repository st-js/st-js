package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * as for @GlobalScope - forbid inner types of global classes - they make no sense
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class ClassGlobalForbidInnerCheck implements CheckContributor<ClassTree> {

	private void checkMember(Tree member, GenerationContext<Void> context) {
		if (member instanceof ClassTree) {
			context.addError(member, "You cannot define an inner type inside a @GlobalScope class. Please define it outside this class.");
		}

	}

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		TreeWrapper<ClassTree, Void> tw = context.getCurrentWrapper();
		if (!tw.isGlobal()) {
			// only applies to global classes
			return null;
		}
		for (Tree member : tree.getMembers()) {
			checkMember(member, context);
		}
		return null;
	}
}
