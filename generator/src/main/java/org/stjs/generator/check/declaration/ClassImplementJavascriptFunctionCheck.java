package org.stjs.generator.check.declaration;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;

/**
 * check that a class does not implement a @JavaScript annotated interface as this is reserved to build inline
 * functions.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class ClassImplementJavascriptFunctionCheck implements CheckContributor<ClassTree> {

	private void checkInteface(TreeWrapper<Tree, Void> iface) {
		if (iface.isJavaScriptFunction()) {
			iface.addError("You cannot implement interfaces annotated with @JavascriptFunction. "
					+ "You can only have inline object creation with this type of interfaces");
		}
	}

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		if (tree.getSimpleName().toString().isEmpty()) {
			// anonymous class is ok
			return null;
		}
		TreeWrapper<ClassTree, Void> tw = context.getCurrentWrapper();
		for (Tree iface : tree.getImplementsClause()) {
			checkInteface(tw.child(iface));
		}
		if (tree.getExtendsClause() != null) {
			TreeWrapper<Tree, Void> superType = tw.child(tree.getExtendsClause());
			if (superType.isJavaScriptFunction()) {
				superType.addError("You cannot extend interfaces annotated with @JavascriptFunction. "
						+ "You can only have inline object creation with this type of interfaces");
			}
		}
		return null;
	}
}
