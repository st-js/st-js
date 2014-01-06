package org.stjs.generator.check.expression;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.expression.NewClassWriter;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.StatementTree;

/**
 * this class checks that only field assignment are present in the initialization like this:<br>
 * new Type(){{x = 1; y = 2; }};
 * 
 * @author acraciun
 */
public class NewClassObjectInitCheck implements CheckContributor<NewClassTree> {

	private void checkStatement(StatementTree stmt, GenerationContext<Void> context) {
		boolean ok = true;
		if (stmt instanceof ExpressionStatementTree) {
			ok = ((ExpressionStatementTree) stmt).getExpression() instanceof AssignmentTree;
		} else {
			ok = false;
		}

		if (!ok) {
			context.addError(stmt, "Only assign expression are allowed in an object creation block");
		}
	}

	@Override
	public Void visit(CheckVisitor visitor, NewClassTree tree, GenerationContext<Void> context) {
		BlockTree initBlock = NewClassWriter.getDoubleBracesBlock(tree);
		TreeWrapper<ClassTree, Void> tw = context.getCurrentWrapper();

		if (initBlock == null && !tw.child(tree.getIdentifier()).isSyntheticType()) {
			return null;
		}

		if (initBlock != null) {
			for (StatementTree stmt : initBlock.getStatements()) {
				checkStatement(stmt, context);
			}
		}
		return null;
	}

}
