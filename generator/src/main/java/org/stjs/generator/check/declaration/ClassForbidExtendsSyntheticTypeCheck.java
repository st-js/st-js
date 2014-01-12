package org.stjs.generator.check.declaration;

import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * this checks that a class or interface does not try to extend or implement a
 * {@link org.stjs.javascript.annotation.SyntheticType} as the code for this class does not exist in reality, so the
 * calls to any method of such a type may end up in a runtime error.
 * 
 * @author acraciun
 */
public class ClassForbidExtendsSyntheticTypeCheck implements CheckContributor<ClassTree> {
	private void checkInterface(TreeWrapper<Tree, Void> iface) {
		if (iface.isSyntheticType()) {
			iface.addError("You cannot implement an interface that is marked as synthetic (@SyntheticType)");
		}
	}

	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		TreeWrapper<ClassTree, Void> tw = context.getCurrentWrapper();
		TypeElement element = (TypeElement) tw.getElement();
		if (element.getNestingKind() == NestingKind.ANONYMOUS) {
			return null;
		}
		if (tree.getExtendsClause() != null && tw.child(tree.getExtendsClause()).isSyntheticType()) {
			context.addError(tree, "You cannot extend from a class that is marked as synthetic (@SyntheticType)");
		}
		for (Tree iface : tree.getImplementsClause()) {
			checkInterface(tw.child(iface));
		}

		return null;
	}

}
