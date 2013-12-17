package org.stjs.generator.check.declaration;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.utils.JavaNodes;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree;

/**
 * check that a class does not implement a @JavaScript annotated interface as this is reserved to build inline
 * functions.
 * @author acraciun
 */
public class ClassImplementJavascriptFunctionCheck implements CheckContributor<ClassTree> {

	@Override
	public Void visit(CheckVisitor visitor, ClassTree tree, GenerationContext<Void> context) {
		if (tree.getSimpleName().toString().isEmpty()) {
			// anonymous class is ok
			return null;
		}
		for (Tree iface : tree.getImplementsClause()) {
			Element ifaceType = TreeUtils.elementFromUse((ExpressionTree) iface);
			if (JavaNodes.isJavaScriptFunction(ifaceType)) {
				context.addError(iface, "You cannot implement intefaces annotated with @JavascriptFunction. "
						+ "You can only have inline object creation with this type of interfaces");
			}
		}
		if (tree.getExtendsClause() != null) {
			Element superType = TreeUtils.elementFromUse((ExpressionTree) tree.getExtendsClause());
			if (JavaNodes.isJavaScriptFunction(superType)) {
				context.addError(tree.getExtendsClause(), "You cannot extend intefaces annotated with @JavascriptFunction. "
						+ "You can only have inline object creation with this type of interfaces");
			}
		}
		return null;
	}
}
