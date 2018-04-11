package org.stjs.generator.check.declaration;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * (c) Swissquote 11.04.18
 *
 * @author sgoetz
 */
public class ClassForbidClassInInterfaceCheck implements CheckContributor<ClassTree> {

	private void checkMember(Tree member, GenerationContext<Void> context) {
		if (member instanceof ClassTree) {
			context.addError(member, "You cannot define an inner type inside an interface. Please define it outside this interface.");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		Element type = TreeUtils.elementFromDeclaration(tree);
		if (type.getKind() != ElementKind.INTERFACE) {
			// only applies to anonymous classes
			return null;
		}
		for (Tree member : tree.getMembers()) {
			checkMember(member, context);
		}
		return null;
	}
}
