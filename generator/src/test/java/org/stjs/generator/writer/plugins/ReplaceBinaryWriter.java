package org.stjs.generator.writer.plugins;

import java.util.Arrays;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BinaryTree;

public class ReplaceBinaryWriter<JS> implements WriterContributor<BinaryTree, JS> {
	@Override
	public JS visit(WriterVisitor<JS> visitor, BinaryTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getLeftOperand(), context);
		// JS right = visitor.scan(tree.getRightOperand(), context);
		BinaryOperator op = BinaryOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		@SuppressWarnings("unchecked")
		// replaces on purpose the second operand at 2
		JS expr = context.js().binary(op, Arrays.asList(left, context.js().number(2)));

		return expr;
	}
}
