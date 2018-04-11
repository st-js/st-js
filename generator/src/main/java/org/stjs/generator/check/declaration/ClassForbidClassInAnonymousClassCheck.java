package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * (c) Swissquote 11.04.18
 *
 * @author sgoetz
 */
public class ClassForbidClassInAnonymousClassCheck implements CheckContributor<ClassTree> {

	private void checkMember(Tree member, GenerationContext<Void> context) {
		if (member instanceof ClassTree) {
			context.addError(member, "You cannot define an inner type inside an anonymous class. Please define it outside this class.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		if (tree.getSimpleName().length() > 0) {
			// only applies to anonymous classes
			return null;
		}
		for (Tree member : tree.getMembers()) {
			checkMember(member, context);
		}
		return null;
	}
}
