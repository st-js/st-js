package org.stjs.generator.check.expression;


import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.expression.NewClassWriter;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.StatementTree;

/**
 * this class checks that only field assignment are present in the initialization like this:<br>
 * new Type(){{x = 1; y = 2; }};
 * @author acraciun
 */
public class NewClassObjectInitCheck implements CheckContributor<NewClassTree> {

	@Override
	public Void visit(CheckVisitor visitor, NewClassTree tree, GenerationContext<Void> context) {
		BlockTree initBlock = NewClassWriter.getDoubleBracesBlock(tree);
		Element type = TreeUtils.elementFromUse(tree.getIdentifier());
		if (initBlock == null && !JavaNodes.isSyntheticType(type)) {
			return null;
		}

		if (initBlock != null) {
			for (StatementTree stmt : initBlock.getStatements()) {
				boolean ok = true;
				if (!(stmt instanceof ExpressionStatementTree)) {
					ok = false;
				} else {
					ok = ((ExpressionStatementTree) stmt).getExpression() instanceof AssignmentTree;
				}

				if (!ok) {
					context.addError(stmt, "Only assign expression are allowed in an object creation block");
				}
			}
		}
		return null;
	}

}
