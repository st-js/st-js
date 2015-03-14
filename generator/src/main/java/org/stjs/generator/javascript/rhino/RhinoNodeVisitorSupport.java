package org.stjs.generator.javascript.rhino;

import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.BreakStatement;
import org.mozilla.javascript.ast.CatchClause;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ContinueStatement;
import org.mozilla.javascript.ast.DoLoop;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.EmptyExpression;
import org.mozilla.javascript.ast.EmptyStatement;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Label;
import org.mozilla.javascript.ast.LabeledStatement;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.SwitchStatement;
import org.mozilla.javascript.ast.ThrowStatement;
import org.mozilla.javascript.ast.TryStatement;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.WhileLoop;

/**
 * 
 * this class helps using a type visitor as the rhino library does not provide one out of the box
 * 
 * @author acraciun
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(justification = "The type check is done by looking in the map", value = "BC_UNCONFIRMED_CAST")
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class RhinoNodeVisitorSupport {
	private static Map<Class<?>, Caller> callers = new HashMap<Class<?>, Caller>();

	private interface Caller {
		<T> void call(Node node, AstVisitor<T> visitor, T param);
	}

	static {
		addCaller(Name.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitName((Name) node, param);
			}
		});
		addCaller(Label.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitLabel((Label) node, param);
			}
		});
		addCaller(Block.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitBlock((Block) node, param);
			}
		});
		addCaller(ElementGet.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitElementGet((ElementGet) node, param);
			}
		});
		addCaller(NewExpression.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitNewExpression((NewExpression) node, param);
			}
		});
		addCaller(ArrayLiteral.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitArrayLiteral((ArrayLiteral) node, param);
			}
		});
		addCaller(StringLiteral.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitStringLiteral((StringLiteral) node, param);
			}
		});
		addCaller(ParenthesizedExpression.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitParenthesizedExpression((ParenthesizedExpression) node, param);
			}
		});
		addCaller(BreakStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitBreakStatemen((BreakStatement) node, param);
			}
		});
		addCaller(SwitchCase.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitSwitchCase((SwitchCase) node, param);
			}
		});
		addCaller(CatchClause.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitCatchClause((CatchClause) node, param);
			}
		});
		addCaller(ContinueStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitContinueStatement((ContinueStatement) node, param);
			}
		});
		addCaller(FunctionNode.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitFunctionNode((FunctionNode) node, param);
			}
		});
		addCaller(ObjectProperty.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitObjectProperty((ObjectProperty) node, param);
			}
		});
		addCaller(ObjectLiteral.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitObjectLiteral((ObjectLiteral) node, param);
			}
		});

		addCaller(DoLoop.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitDoLoop((DoLoop) node, param);
			}
		});
		addCaller(EmptyStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitEmptyStatement((EmptyStatement) node, param);
			}
		});
		addCaller(ForInLoop.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitForInLoop((ForInLoop) node, param);
			}
		});
		addCaller(ForLoop.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitForLoop((ForLoop) node, param);
			}
		});
		addCaller(IfStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitIfStatement((IfStatement) node, param);
			}
		});
		addCaller(LabeledStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitLabeledStatement((LabeledStatement) node, param);
			}
		});

		addCaller(ReturnStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitReturnStatement((ReturnStatement) node, param);
			}
		});
		addCaller(SwitchStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitSwitchStatement((SwitchStatement) node, param);
			}
		});
		addCaller(FunctionCall.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitFunctionCall((FunctionCall) node, param);
			}
		});
		addCaller(PropertyGet.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitPropertyGet((PropertyGet) node, param);
			}
		});
		addCaller(VariableDeclaration.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitVariableDeclaration((VariableDeclaration) node, param);
			}
		});
		addCaller(VariableInitializer.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitVariableInitializer((VariableInitializer) node, param);
			}
		});

		addCaller(TryStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitTryStatement((TryStatement) node, param);
			}
		});
		addCaller(WhileLoop.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitWhileLoop((WhileLoop) node, param);
			}
		});
		addCaller(AstRoot.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitAstRoot((AstRoot) node, param);
			}
		});
		addCaller(ConditionalExpression.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitConditionalExpression((ConditionalExpression) node, param);
			}
		});
		addCaller(NumberLiteral.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitNumberLitera((NumberLiteral) node, param);
			}
		});
		addCaller(Statements.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitStatements((Statements) node, param);
			}
		});

		addCaller(Assignment.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitAssignment((Assignment) node, param);
			}
		});
		addCaller(InfixExpression.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitInfixExpression((InfixExpression) node, param);
			}
		});
		addCaller(KeywordLiteral.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitKeywordLiteral((KeywordLiteral) node, param);
			}
		});
		addCaller(UnaryExpression.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitUnaryExpression((UnaryExpression) node, param);
			}
		});
		addCaller(ExpressionStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitExpressionStatement((ExpressionStatement) node, param);
			}
		});
		addCaller(ThrowStatement.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitThrowStatement((ThrowStatement) node, param);
			}
		});
		addCaller(CodeFragment.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitCodeFragment((CodeFragment) node, param);
			}
		});
		addCaller(EmptyExpression.class, new Caller() {
			@Override
			public <T> void call(Node node, AstVisitor<T> visitor, T param) {
				visitor.visitEmptyExpression((EmptyExpression) node, param);
			}
		});
	}

	public <T> void accept(Node node, AstVisitor<T> visitor, T param) {
		Caller caller = callers.get(node.getClass());
		if (caller != null) {
			caller.call(node, visitor, param);
		}
	}

	private static void addCaller(Class<?> nodeClass, Caller caller) {
		callers.put(nodeClass, caller);
	}
}
