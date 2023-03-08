package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.MemberSelectTree;

/**
 * @author acraciun
 */
public class MemberSelectGlobalScopeNameClashCheck implements CheckContributor<MemberSelectTree> {

	@Override
	public Void visit(CheckVisitor visitor, MemberSelectTree tree, GenerationContext<Void> context) {
		return IdentifierGlobalScopeNameClashCheck.checkGlobalScope(tree, tree.getIdentifier().toString(), context);
	}

}
