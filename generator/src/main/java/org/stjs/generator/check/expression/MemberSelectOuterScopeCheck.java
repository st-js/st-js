package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MemberSelectTree;

/**
 * this check that you don't try to access the outer this using Type.this.
 * 
 * @author acraciun
 * 
 */
public class MemberSelectOuterScopeCheck implements VisitorContributor<MemberSelectTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, MemberSelectTree tree, GenerationContext context, Void prev) {
		if (tree.getIdentifier().toString().equals(GeneratorConstants.THIS)) {
			context.addError(tree, "In Javascript you cannot call methods or fields from the outer type. "
					+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}
		return null;
	}

}
