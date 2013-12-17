package org.stjs.generator.writer.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javacutils.TypesUtils;

import javax.lang.model.type.TypeMirror;

import org.mozilla.javascript.Token;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;

public class BinaryWriter<JS> implements WriterContributor<BinaryTree, JS> {
	private static Map<Kind, Integer> jsOperators = new HashMap<Kind, Integer>();

	static {
		jsOperators.put(Kind.MULTIPLY, Token.MUL);
		jsOperators.put(Kind.DIVIDE, Token.DIV);
		jsOperators.put(Kind.REMAINDER, Token.MOD);
		jsOperators.put(Kind.PLUS, Token.ADD);
		jsOperators.put(Kind.MINUS, Token.DEC);

		jsOperators.put(Kind.LEFT_SHIFT, Token.LSH);
		jsOperators.put(Kind.RIGHT_SHIFT, Token.RSH);
		jsOperators.put(Kind.UNSIGNED_RIGHT_SHIFT, Token.URSH);

		jsOperators.put(Kind.LESS_THAN, Token.LT);
		jsOperators.put(Kind.LESS_THAN_EQUAL, Token.LE);
		jsOperators.put(Kind.GREATER_THAN, Token.GT);
		jsOperators.put(Kind.GREATER_THAN_EQUAL, Token.GE);
		jsOperators.put(Kind.EQUAL_TO, Token.EQ);
		jsOperators.put(Kind.NOT_EQUAL_TO, Token.NE);

		jsOperators.put(Kind.AND, Token.BITAND);
		jsOperators.put(Kind.XOR, Token.BITXOR);
		jsOperators.put(Kind.OR, Token.BITOR);
		jsOperators.put(Kind.CONDITIONAL_AND, Token.AND);
		jsOperators.put(Kind.CONDITIONAL_OR, Token.OR);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, BinaryTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getLeftOperand(), context);
		JS right = visitor.scan(tree.getRightOperand(), context);
		Integer op = jsOperators.get(tree.getKind());
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
