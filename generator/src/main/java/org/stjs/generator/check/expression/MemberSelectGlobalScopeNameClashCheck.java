package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.MemberSelectTree;

/**
 * @see {@link IdentifierGlobalScopeNameClashCheck}
 * @author acraciun
 */
public class MemberSelectGlobalScopeNameClashCheck implements VisitorContributor<MemberSelectTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, MemberSelectTree tree, GenerationContext context, Void prev) {
		return IdentifierGlobalScopeNameClashCheck.checkGlobalScope(tree, tree.getIdentifier().toString(), context);
	}

}
