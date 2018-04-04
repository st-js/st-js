package org.stjs.generator.writer.templates.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.Keyword;
import org.stjs.generator.javascript.UnaryOperator;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.UnaryTree;

/**
 * Java unary operator
 * @author acraciun
 */
public class SetterUnaryTemplate<JS> extends DefaultUnaryTemplate<JS> {

	@Override
	public JS visit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context) {
		return doVisit(visitor, tree, context, false);
	}

	protected BinaryOperator getBinaryOperator(UnaryOperator op) {
		if (op == UnaryOperator.PREFIX_DECREMENT || op == UnaryOperator.POSTFIX_DECREMENT) {
			return BinaryOperator.MINUS;
		}
		if (op == UnaryOperator.PREFIX_INCREMENT || op == UnaryOperator.POSTFIX_INCREMENT) {
			return BinaryOperator.PLUS;
		}
		return null;
	}

	protected JS doVisit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context, boolean global) {
		UnaryOperator op = UnaryOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		BinaryOperator binaryOp = getBinaryOperator(op);

		if (binaryOp == null) {
			return super.visit(visitor, tree, context);
		}

		JS operand = visitor.scan(tree.getExpression(), context);
		TreeWrapper<ExpressionTree, JS> twOperand = context.getCurrentWrapper().child(tree.getExpression());
		JS target = SetterAssignmentTemplate.getTarget(visitor, twOperand, context);
		JS field = SetterAssignmentTemplate.getField(twOperand, context);

		JS value = context.js().binary(binaryOp, Arrays.asList(operand, context.js().number(1)));

		if (global) {
			JS eg = context.js().elementGet(target, field);
			return context.js().assignment(AssignOperator.ASSIGN, eg, value);
		}

		List<JS> arguments = new ArrayList<JS>();
		arguments.add(field);
		arguments.add(value);
		if (op.isPostfix()) {
			arguments.add(context.js().keyword(Keyword.TRUE));
		}

		return context.js().functionCall(context.js().property(target, "set"), arguments);

	}
}
