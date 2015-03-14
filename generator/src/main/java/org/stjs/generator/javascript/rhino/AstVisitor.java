package org.stjs.generator.javascript.rhino;

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

public interface AstVisitor<T> {
	void visitName(Name name, T param);

	void visitLabel(Label label, T param);

	void visitBlock(Block block, T param);

	void visitElementGet(ElementGet eg, T param);

	void visitNewExpression(NewExpression ne, T param);

	void visitArrayLiteral(ArrayLiteral a, T param);

	void visitStringLiteral(StringLiteral expr, T param);

	void visitParenthesizedExpression(ParenthesizedExpression p, T param);

	void visitBreakStatemen(BreakStatement b, T param);

	void visitSwitchCase(SwitchCase s, T param);

	void visitCatchClause(CatchClause c, T param);

	void visitContinueStatement(ContinueStatement c, T param);

	void visitFunctionNode(FunctionNode f, T param);

	void visitObjectProperty(ObjectProperty p, T param);

	void visitObjectLiteral(ObjectLiteral p, T param);

	void visitDoLoop(DoLoop d, T param);

	void visitEmptyStatement(EmptyStatement s, T param);

	void visitEmptyExpression(EmptyExpression s, T param);

	void visitForInLoop(ForInLoop f, T param);

	void visitForLoop(ForLoop f, T param);

	void visitIfStatement(IfStatement ifs, T param);

	void visitLabeledStatement(LabeledStatement label, T param);

	void visitReturnStatement(ReturnStatement r, T param);

	void visitSwitchStatement(SwitchStatement s, T param);

	void visitFunctionCall(FunctionCall fc, T param);

	void visitPropertyGet(PropertyGet p, T param);

	void visitVariableDeclaration(VariableDeclaration v, T param);

	void visitVariableInitializer(VariableInitializer v, T param);

	void visitTryStatement(TryStatement t, T param);

	void visitWhileLoop(WhileLoop w, T param);

	void visitAstRoot(AstRoot r, T param);

	void visitConditionalExpression(ConditionalExpression c, T param);

	void visitNumberLitera(NumberLiteral n, T param);

	void visitStatements(Statements s, T param);

	void visitAssignment(Assignment a, T param);

	void visitInfixExpression(InfixExpression ie, T param);

	void visitKeywordLiteral(KeywordLiteral k, T param);

	void visitUnaryExpression(UnaryExpression u, T param);

	void visitExpressionStatement(ExpressionStatement e, T param);

	void visitThrowStatement(ThrowStatement e, T param);

	void visitCodeFragment(CodeFragment c, T param);
}
