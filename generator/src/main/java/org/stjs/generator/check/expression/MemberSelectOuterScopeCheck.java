package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.GeneratorConstants;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.MemberSelectTree;

/**
 * this check that you don't try to access the outer this using Type.this.
 * @author acraciun
 */
public class MemberSelectOuterScopeCheck implements CheckContributor<MemberSelectTree> {

	@Override
	public Void visit(CheckVisitor visitor, MemberSelectTree tree, GenerationContext<Void> context) {
		if (tree.getIdentifier().toString().equals(GeneratorConstants.THIS)) {
			context.addError(tree, "In Javascript you cannot call methods or fields from the outer type. "
					+ "You should define a variable var that=this outside your function definition and call the methods on this object");
		}
		return null;
	}

}
