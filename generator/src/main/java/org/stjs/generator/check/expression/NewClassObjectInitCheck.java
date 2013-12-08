package org.stjs.generator.check.expression;

import javacutils.TreeUtils;

import javax.lang.model.element.Element;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;
import org.stjs.generator.writer.expression.NewClassWriter;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.StatementTree;

/**
 * this class checks that only field assignment are present in the initialization like this:<br>
 * new Type(){{x = 1; y = 2; }};
 * 
 * @author acraciun
 * 
 */
public class NewClassObjectInitCheck implements VisitorContributor<NewClassTree, Void, GenerationContext> {

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, NewClassTree tree, GenerationContext context, Void prev) {
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
