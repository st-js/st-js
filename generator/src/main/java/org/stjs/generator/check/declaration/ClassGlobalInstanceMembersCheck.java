package org.stjs.generator.check.declaration;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.InternalUtils;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * as for @GlobalScope - global classes, there is not class declaration, the members are attached directly to the
 * JavaScript global object (i.e. the window when running in the browser), the instance members have no meaning, so
 * forbid them.
 * 
 * @author acraciun
 */
public class ClassGlobalInstanceMembersCheck implements CheckContributor<ClassTree> {

	private void checkMember(Tree member, GenerationContext<Void> context) {
		if (member instanceof BlockTree || InternalUtils.isSyntheticConstructor(member)) {
			return;
		}
		Element memberElement = JavaNodes.elementFromDeclaration(member);
		if (!JavaNodes.isStatic(memberElement)) {
			context.addError(member, "Only static constructions can be used in a @GlobalScope class");
		}

	}

	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		Element element = TreeUtils.elementFromDeclaration(tree);
		if (!JavaNodes.isGlobal(element)) {
			// only applies to global classes
			return null;
		}
		for (Tree member : tree.getMembers()) {
			checkMember(member, context);
		}
		return null;
	}

}
