package org.stjs.generator.check.expression;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Scope;

public class IdentifierAccessOuterScopeCheck implements VisitorContributor<IdentifierTree, Void, GenerationContext> {
	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, IdentifierTree tree, GenerationContext context, Void prev) {
		Element fieldElement = TreeUtils.elementFromUse(tree);
		if (fieldElement == null || fieldElement.getKind() != ElementKind.FIELD) {
			//only meant for fields
			return null;
		}
		if (JavaNodes.isStatic(fieldElement)) {
			//only instance fieds
			return null;
		}
		Scope currentScope = context.getTrees().getScope(context.getCurrentPath());
		if (currentScope.getEnclosingClass() != fieldElement.getEnclosingElement()) {
			context.addError(tree, "In Javascript you cannot call methods or fields from the outer type. "
					+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}
		return null;
	}

}
