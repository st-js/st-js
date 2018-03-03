package org.stjs.generator.writer.expression;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.Collections;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TypesUtils;
import org.stjs.generator.javascript.BinaryOperator;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;

public class BinaryWriter<JS> implements WriterContributor<BinaryTree, JS> {
	@Override
	public JS visit(WriterVisitor<JS> visitor, BinaryTree tree, GenerationContext<JS> context) {
	    JS left = visitor.scan(tree.getLeftOperand(), context);
        JS right = visitor.scan(tree.getRightOperand(), context);
		BinaryOperator op = BinaryOperator.valueOf(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();


		TypeMirror leftType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getLeftOperand()));
		TypeMirror rightType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getRightOperand()));
		JavaScriptBuilder<JS> b = context.js();
		if ("java.lang.String".equals(leftType.toString()) && rightType.getKind() == TypeKind.CHAR) {
            right = b.functionCall(b.property(b.name("String"), "fromCharCode"), asList(right));
		} else if ("java.lang.String".equals(rightType.toString()) && leftType.getKind() == TypeKind.CHAR) {
            left = b.functionCall(b.property(b.name("String"), "fromCharCode"), asList(left));
        }

		boolean integerDivision = tree.getKind() == Kind.DIVIDE && TypesUtils.isIntegral(leftType) && TypesUtils.isIntegral(rightType);

		@SuppressWarnings("unchecked")
        JS expr = b.binary(op, asList(left, right));

		if (integerDivision) {
			// force a cast for integer division to have the expected behavior in JavaScript too
			JS target = b.property(b.name("stjs"), "trunc");
			return b.functionCall(target, Collections.singleton(expr));
		}
		return expr;
	}
}
