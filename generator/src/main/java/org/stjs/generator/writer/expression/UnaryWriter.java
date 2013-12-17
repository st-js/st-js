package org.stjs.generator.writer.expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.UnaryExpression;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;

import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.UnaryTree;

public class UnaryWriter<JS> implements WriterContributor<UnaryTree, JS> {
	private static Map<Kind, JavaScriptUnaryOperator> jsOperators = new HashMap<Kind, JavaScriptUnaryOperator>();

	private static class JavaScriptUnaryOperator {
		private final boolean postfix;
		private final int operator;

		public JavaScriptUnaryOperator(boolean postfix, int operator) {
			this.postfix = postfix;
			this.operator = operator;
		}

		public boolean isPostfix() {
			return postfix;
		}

		public int getOperator() {
			return operator;
		}
	}

	static {

		jsOperators.put(Kind.POSTFIX_INCREMENT, new JavaScriptUnaryOperator(true, Token.INC));
		jsOperators.put(Kind.POSTFIX_DECREMENT, new JavaScriptUnaryOperator(true, Token.DEC));
		jsOperators.put(Kind.PREFIX_INCREMENT, new JavaScriptUnaryOperator(false, Token.INC));
		jsOperators.put(Kind.PREFIX_INCREMENT, new JavaScriptUnaryOperator(false, Token.DEC));

		jsOperators.put(Kind.UNARY_PLUS, new JavaScriptUnaryOperator(false, Token.ADD));
		jsOperators.put(Kind.UNARY_MINUS, new JavaScriptUnaryOperator(false, Token.SUB));
		jsOperators.put(Kind.BITWISE_COMPLEMENT, new JavaScriptUnaryOperator(false, Token.BITNOT));
		jsOperators.put(Kind.LOGICAL_COMPLEMENT, new JavaScriptUnaryOperator(false, Token.NOT));

	}

	@Override
	public JS visit(WriterVisitor<JS> visitor, UnaryTree tree, GenerationContext<JS> context) {
		UnaryExpression expr = new UnaryExpression();
		expr.setOperand(visitor.scan(tree.getExpression(), p).get(0));
		JavaScriptUnaryOperator op = jsOperators.get(tree.getKind());
		assert op != null : "Unknow operator:" + tree.getKind();

		expr.setIsPostfix(op.isPostfix());
		expr.setOperator(op.getOperator());
		return Collections.<AstNode>singletonList(expr);
	}
}
