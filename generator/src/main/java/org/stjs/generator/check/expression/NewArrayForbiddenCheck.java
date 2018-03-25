package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;

import com.sun.source.tree.NewArrayTree;

/**
 * this checks that no java array is used. You should use {@link org.stjs.javascript.Array} instead.
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class NewArrayForbiddenCheck implements CheckContributor<NewArrayTree> {

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, NewArrayTree tree, GenerationContext<Void> context) {
		return null;
	}
}
