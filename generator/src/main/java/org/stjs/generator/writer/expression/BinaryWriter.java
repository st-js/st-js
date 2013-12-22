package org.stjs.generator.writer.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;

public class BinaryWriter<JS> implements WriterContributor<BinaryTree, JS> {
	private static Map<Kind, Integer> jsOperators = new HashMap<Kind, Integer>();

	@Override
	public JS visit(WriterVisitor<JS> visitor, BinaryTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getLeftOperand(), context);
		JS right = visitor.scan(tree.getRightOperand(), context);
		BinaryOperator op = BinaryOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		@SuppressWarnings("unchecked")
		JS expr = context.js().binary(op, Arrays.asList(left, right));

		TypeMirror leftType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getLeftOperand()));
		TypeMirror rightType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getRightOperand()));
		boolean integerDivision = tree.getKind() == Kind.DIVIDE && TypesUtils.isIntegral(leftType) && TypesUtils.isIntegral(rightType);

		if (integerDivision) {
			// force a cast for integer division to have the expected behavior in JavaScript too
			JS target = context.js().property(context.js().name("stjs"), "trunc");
			return context.js().functionCall(target, Collections.singleton(expr));
		}
		return expr;
	}
}
