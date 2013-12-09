package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;

/**
 * this class checks that you use only literals for a map key: i.e. $map(variable, 1) is not allowed but $map("key", 1)
 * is allowed
 * @author acraciun
 */
public class MethodInvocationMapConstructorCheck implements VisitorContributor<MethodInvocationTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, MethodInvocationTree tree, GenerationContext context,
			Void prev) {
		String templateName = MethodInvocationWriter.buildTemplateName(tree, context);
		if ("map".equals(templateName)) {
			for (int i = 0; i < tree.getArguments().size(); i += 2) {
				if (!(tree.getArguments().get(i) instanceof LiteralTree)) {
					context.addError(tree.getArguments().get(i), "The key of a map built this way can only be a literal. "
							+ "Use map.$put(variable) if you want to use variables as keys");
				}
			}
		}
		return null;
	}

}
