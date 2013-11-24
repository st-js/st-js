package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.Tree.Kind;

public class CompoundAssignmentWriter implements VisitorContributor<CompoundAssignmentTree, List<AstNode>, GenerationContext> {
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
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, CompoundAssignmentTree tree,
			GenerationContext p, List<AstNode> prev) {
		Assignment expr = new Assignment();
		expr.setLeft(visitor.scan(tree.getVariable(), p).get(0));
		expr.setRight(visitor.scan(tree.getExpression(), p).get(0));
		Integer op = jsOperators.get(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		expr.setOperator(op);
		return Collections.<AstNode>singletonList(expr);
	}
}
