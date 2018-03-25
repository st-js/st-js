package org.stjs.generator.check.expression;

import javax.lang.model.element.ExecutableElement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.check.CheckContributor;
import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.expression.NewClassWriter;
import org.stjs.javascript.annotation.Template;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.StatementTree;

/**
 * this class checks that only field assignment are present in the initialization like this:<br>
 * new Type(){{x = 1; y = 2; }};
 *
 * @author acraciun
 * @version $Id: $Id
 */
public class NewClassObjectInitCheck implements CheckContributor<NewClassTree> {

	private void checkStatement(StatementTree stmt, GenerationContext<Void> context) {
		boolean ok = true;
		if (stmt instanceof ExpressionStatementTree) {
			ExpressionTree expr = ((ExpressionStatementTree) stmt).getExpression();
			ok = isPlainAssignExpression(expr) || isTemplateAssignExpression(expr);
		} else {
			ok = false;
		}

		if (!ok) {
			context.addError(stmt, "Only assign expression (or equivalent) are allowed in an object creation block");
		}
	}

	private boolean isPlainAssignExpression(ExpressionTree expr) {
		return expr instanceof AssignmentTree;
	}

	private boolean isTemplateAssignExpression(ExpressionTree expr) {
		// the expression must be a method call.
		if (!(expr instanceof MethodInvocationTree)) {
			return false;
		}
		MethodInvocationTree meth = (MethodInvocationTree) expr;

		// The method call must be a call without selectors (so directly the method name)
		ExpressionTree select = meth.getMethodSelect();
		if (!(select instanceof IdentifierTree)) {
			return false;
		}

		// The method call must have exactly one argument
		if (meth.getArguments().size() != 1) {
			return false;
		}

		// the method being called must be annotated with @Template
		ExecutableElement elem = TreeUtils.elementFromUse(meth);
		Template template = elem.getAnnotation(Template.class);
		if (template == null) {
			return false;
		}

		// The value of the @Template annotation must be "toProperty"
		if (!"toProperty".equals(template.value())) {
			return false;
		}

		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Void visit(CheckVisitor visitor, NewClassTree tree, GenerationContext<Void> context) {
		BlockTree initBlock = NewClassWriter.getDoubleBracesBlock(tree);
		TreeWrapper<ClassTree, Void> tw = context.getCurrentWrapper();

		if (initBlock == null && !tw.child(tree.getIdentifier()).isSyntheticType()) {
			return null;
		}

		if (initBlock != null) {
			if (!tw.child(tree.getIdentifier()).isSyntheticType()) {
				context.addError(tree, "Object creation block (double braces {{}}) is only allowed for classes annotated with @SyntheticType");
			}

			for (StatementTree stmt : initBlock.getStatements()) {
				checkStatement(stmt, context);
			}
		}
		return null;
	}

}
