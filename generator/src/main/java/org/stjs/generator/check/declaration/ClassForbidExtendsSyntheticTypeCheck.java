package org.stjs.generator.check.declaration;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.javascript.annotation.SyntheticType;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree;

/**
 * this checks that a class or interface does not try to extend or implement a {@link SyntheticType} as the code for
 * this class does not exist in reality, so the calls to any method of such a type may end up in a runtime error.
 * 
 * @author acraciun
 * 
 */
public class ClassForbidExtendsSyntheticTypeCheck implements VisitorContributor<ClassTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, ClassTree tree, GenerationContext context, Void prev) {
		TypeElement element = TreeUtils.elementFromDeclaration(tree);
		if (element.getNestingKind() == NestingKind.ANONYMOUS) {
			return null;
		}
		if (tree.getExtendsClause() != null) {
			Element superType = TreeUtils.elementFromUse((ExpressionTree) tree.getExtendsClause());
			if (JavaNodes.isSyntheticType(superType)) {
				context.addError(tree, "You cannot extend from a class that is marked as synthetic (@SyntheticType)");
			}
		}
		for (Tree iface : tree.getImplementsClause()) {
			Element ifaceType = TreeUtils.elementFromUse((ExpressionTree) iface);
			if (JavaNodes.isSyntheticType(ifaceType)) {
				context.addError(tree, "You cannot implement an interface that is marked as synthetic (@SyntheticType)");
			}
		}

		return null;
	}

}
