package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.MemberSelectTree;

/**
 * <p>
 * MemberSelectGlobalScopeNameClashCheck class.
 * </p>
 *
 * see {@link org.stjs.generator.check.expression.IdentifierGlobalScopeNameClashCheck}
 * 
 * @author acraciun
 * @version $Id: $Id
 */
public class MemberSelectGlobalScopeNameClashCheck implements CheckContributor<MemberSelectTree> {

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, MemberSelectTree tree, GenerationContext<Void> context) {
		return IdentifierGlobalScopeNameClashCheck.checkGlobalScope(tree, tree.getIdentifier().toString(), context);
	}

}
