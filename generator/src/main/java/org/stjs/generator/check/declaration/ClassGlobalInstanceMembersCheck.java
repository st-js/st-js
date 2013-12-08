package org.stjs.generator.check.declaration;

import javacutils.InternalUtils;
import javacutils.TreeUtils;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * as for @GlobalScope - global classes, there is not class declaration, the members are attached directly to the
 * JavaScript global object (i.e. the window when running in the browser), the instance members have no meaning, so
 * forbid them.
 * 
 * @author acraciun
 * 
 */
public class ClassGlobalInstanceMembersCheck implements VisitorContributor<ClassTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, ClassTree tree, GenerationContext context, Void prev) {
		Element element = TreeUtils.elementFromDeclaration(tree);
		if (!JavaNodes.isGlobal(element)) {
			// only applies to global classes
			return null;
		}
		for (Tree member : tree.getMembers()) {
			if (member instanceof BlockTree || InternalUtils.isSyntheticConstructor(member)) {
				continue;
			}
			Element memberElement = JavaNodes.elementFromDeclaration(member);
			if (!JavaNodes.isStatic(memberElement)) {
				context.addError(member, "Only static constructions can be used in a @GlobalScope class");
			}

		}
		return null;
	}

}
