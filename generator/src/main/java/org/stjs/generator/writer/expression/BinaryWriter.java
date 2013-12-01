package org.stjs.generator.writer.expression;

import static org.stjs.generator.writer.JavaScriptNodes.functionCall;
import static org.stjs.generator.writer.JavaScriptNodes.name;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javacutils.TypesUtils;

import javax.lang.model.type.TypeMirror;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;

public class BinaryWriter implements VisitorContributor<BinaryTree, List<AstNode>, GenerationContext> {
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
	public List<AstNode> visit(TreePathScannerContributors<List<AstNode>, GenerationContext> visitor, BinaryTree tree,
			GenerationContext context, List<AstNode> prev) {

		InfixExpression expr = new InfixExpression();
		expr.setLeft(visitor.scan(tree.getLeftOperand(), context).get(0));
		expr.setRight(visitor.scan(tree.getRightOperand(), context).get(0));
		Integer op = jsOperators.get(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();
		expr.setOperator(op);

		TypeMirror leftType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getLeftOperand()));
		TypeMirror rightType = context.getTrees().getTypeMirror(new TreePath(context.getCurrentPath(), tree.getRightOperand()));
		boolean integerDivision = tree.getKind() == Kind.DIVIDE && TypesUtils.isIntegral(leftType) && TypesUtils.isIntegral(rightType);

		if (integerDivision) {
			// force a cast for integer division to have the expected behavior in JavaScript too
			return Collections.<AstNode>singletonList(functionCall(name("stjs"), "trunc", expr));
		}
		return Collections.<AstNode>singletonList(expr);
	}
}
