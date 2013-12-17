package org.stjs.generator.writer.expression;

import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.Tree.Kind;

public class CompoundAssignmentWriter<JS> implements WriterContributor<CompoundAssignmentTree, JS> {
	private static Map<Kind, Integer> jsOperators = new HashMap<Kind, Integer>();

	static {
		jsOperators.put(Kind.MULTIPLY_ASSIGNMENT, Token.ASSIGN_MUL);
		jsOperators.put(Kind.DIVIDE_ASSIGNMENT, Token.ASSIGN_DIV);
		jsOperators.put(Kind.REMAINDER_ASSIGNMENT, Token.ASSIGN_MOD);
		jsOperators.put(Kind.PLUS_ASSIGNMENT, Token.ASSIGN_ADD);
		jsOperators.put(Kind.MINUS_ASSIGNMENT, Token.ASSIGN_SUB);
		jsOperators.put(Kind.LEFT_SHIFT_ASSIGNMENT, Token.ASSIGN_LSH);
		jsOperators.put(Kind.RIGHT_SHIFT_ASSIGNMENT, Token.ASSIGN_RSH);
		jsOperators.put(Kind.UNSIGNED_RIGHT_SHIFT_ASSIGNMENT, Token.ASSIGN_URSH);
		jsOperators.put(Kind.AND_ASSIGNMENT, Token.ASSIGN_BITAND);
		jsOperators.put(Kind.XOR_ASSIGNMENT, Token.ASSIGN_BITXOR);
		jsOperators.put(Kind.OR_ASSIGNMENT, Token.ASSIGN_BITOR);
	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, CompoundAssignmentTree tree, GenerationContext<JS> context) {
		JS left = visitor.scan(tree.getVariable(), context);
		JS right = visitor.scan(tree.getExpression(), context);
		Integer op = jsOperators.get(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		return context.js().assignment(op, left, right);
	}
}
