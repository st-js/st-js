package org.stjs.generator.check.declaration;

import javacutils.InternalUtils;
import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * this class checks that no member is added to an enum declaration (other than of course the enum constants)
 * @author acraciun
 */
public class ClassEnumWithoutMembersCheck implements VisitorContributor<ClassTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, ClassTree tree, GenerationContext context, Void prev) {
		TypeElement typeElement = TreeUtils.elementFromDeclaration(tree);
		if (typeElement.getKind() != ElementKind.ENUM) {
			return null;
		}
		for (Tree member : tree.getMembers()) {
			if (InternalUtils.isSyntheticConstructor(member)) {
				continue;
			}
			boolean ok = false;
			if (member instanceof VariableTree) {
				Element memberElement = TreeUtils.elementFromDeclaration((VariableTree) member);
				ok = memberElement.getKind() == ElementKind.ENUM_CONSTANT;
			}
			if (!ok) {
				context.addError(member, "Enums with fields or methods are not supported");
			}
		}
		return null;
	}
}
