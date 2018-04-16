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
import org.stjs.generator.javascript.rhino.types.ClassDeclaration;
import org.stjs.generator.javascript.rhino.types.Enum;
import org.stjs.generator.javascript.rhino.types.FieldNode;
import org.stjs.generator.javascript.rhino.types.GenericType;
import org.stjs.generator.javascript.rhino.types.InterfaceDeclaration;
import org.stjs.generator.javascript.rhino.types.MethodNode;
import org.stjs.generator.javascript.rhino.types.ParamNode;
import org.stjs.generator.javascript.rhino.types.TypeVariableNode;
import org.stjs.generator.javascript.rhino.types.Vararg;

/**
 * <p>AstVisitor interface.</p>
 *
 * @author acraciun
 * @version $Id: $Id
 */
public interface AstVisitor<T> {
	/**
	 * <p>visitName.</p>
	 *
	 * @param name a {@link org.mozilla.javascript.ast.Name} object.
	 * @param param a T object.
	 */
	void visitName(Name name, T param);

	/**
	 * <p>visitLabel.</p>
	 *
	 * @param label a {@link org.mozilla.javascript.ast.Label} object.
	 * @param param a T object.
	 */
	void visitLabel(Label label, T param);

	/**
	 * <p>visitBlock.</p>
	 *
	 * @param block a {@link org.mozilla.javascript.ast.Block} object.
	 * @param param a T object.
	 */
	void visitBlock(Block block, T param);

	/**
	 * <p>visitElementGet.</p>
	 *
	 * @param eg a {@link org.mozilla.javascript.ast.ElementGet} object.
	 * @param param a T object.
	 */
	void visitElementGet(ElementGet eg, T param);

	/**
	 * <p>visitNewExpression.</p>
	 *
	 * @param ne a {@link org.mozilla.javascript.ast.NewExpression} object.
	 * @param param a T object.
	 */
	void visitNewExpression(NewExpression ne, T param);

	/**
	 * <p>visitArrayLiteral.</p>
	 *
	 * @param a a {@link org.mozilla.javascript.ast.ArrayLiteral} object.
	 * @param param a T object.
	 */
	void visitArrayLiteral(ArrayLiteral a, T param);

	/**
	 * <p>visitStringLiteral.</p>
	 *
	 * @param expr a {@link org.mozilla.javascript.ast.StringLiteral} object.
	 * @param param a T object.
	 */
	void visitStringLiteral(StringLiteral expr, T param);

	/**
	 * <p>visitParenthesizedExpression.</p>
	 *
	 * @param p a {@link org.mozilla.javascript.ast.ParenthesizedExpression} object.
	 * @param param a T object.
	 * @param param a T object.
	 */
	void visitParenthesizedExpression(ParenthesizedExpression p, T param);

	/**
	 * <p>visitBreakStatemen.</p>
	 *
	 * @param b a {@link org.mozilla.javascript.ast.BreakStatement} object.
	 * @param param a T object.
	 */
	void visitBreakStatemen(BreakStatement b, T param);

	/**
	 * <p>visitSwitchCase.</p>
	 *
	 * @param s a {@link org.mozilla.javascript.ast.SwitchCase} object.
	 * @param param a T object.
	 */
	void visitSwitchCase(SwitchCase s, T param);

	/**
	 * <p>visitCatchClause.</p>
	 *
	 * @param c a {@link org.mozilla.javascript.ast.CatchClause} object.
	 * @param param a T object.
	 */
	void visitCatchClause(CatchClause c, T param);

	/**
	 * <p>visitContinueStatement.</p>
	 *
	 * @param c a {@link org.mozilla.javascript.ast.ContinueStatement} object.
	 * @param param a T object.
	 */
	void visitContinueStatement(ContinueStatement c, T param);

	/**
	 * <p>visitFunctionNode.</p>
	 *
	 * @param f a {@link org.mozilla.javascript.ast.FunctionNode} object.
	 * @param param a T object.
	 */
	void visitFunctionNode(FunctionNode f, T param);

	/**
	 * <p>visitInterfaceFunctionNode.</p>
	 *
	 * @param f a {@link org.stjs.generator.javascript.rhino.types.MethodNode} object.
	 * @param param a T object.
	 */
	void visitMethodNode(MethodNode f, T param);

	/**
	 * <p>visitObjectProperty.</p>
	 *
	 * @param p a {@link org.mozilla.javascript.ast.ObjectProperty} object.
	 * @param param a T object.
	 * @param param a T object.
	 */
	void visitObjectProperty(ObjectProperty p, T param);

	/**
	 * <p>visitObjectLiteral.</p>
	 *
	 * @param p a {@link org.mozilla.javascript.ast.ObjectLiteral} object.
	 * @param param a T object.
	 * @param param a T object.
	 */
	void visitObjectLiteral(ObjectLiteral p, T param);

	/**
	 * <p>visitDoLoop.</p>
	 *
	 * @param d a {@link org.mozilla.javascript.ast.DoLoop} object.
	 * @param param a T object.
	 */
	void visitDoLoop(DoLoop d, T param);

	/**
	 * <p>visitEmptyStatement.</p>
	 *
	 * @param s a {@link org.mozilla.javascript.ast.EmptyStatement} object.
	 * @param param a T object.
	 */
	void visitEmptyStatement(EmptyStatement s, T param);

	/**
	 * <p>visitEmptyExpression.</p>
	 *
	 * @param s a {@link org.mozilla.javascript.ast.EmptyExpression} object.
	 * @param param a T object.
	 */
	void visitEmptyExpression(EmptyExpression s, T param);

	/**
	 * <p>visitForInLoop.</p>
	 *
	 * @param f a {@link org.mozilla.javascript.ast.ForInLoop} object.
	 * @param param a T object.
	 */
	void visitForInLoop(ForInLoop f, T param);

	/**
	 * <p>visitForLoop.</p>
	 *
	 * @param f a {@link org.mozilla.javascript.ast.ForLoop} object.
	 * @param param a T object.
	 */
	void visitForLoop(ForLoop f, T param);

	/**
	 * <p>visitIfStatement.</p>
	 *
	 * @param ifs a {@link org.mozilla.javascript.ast.IfStatement} object.
	 * @param param a T object.
	 */
	void visitIfStatement(IfStatement ifs, T param);

	/**
	 * <p>visitLabeledStatement.</p>
	 *
	 * @param label a {@link org.mozilla.javascript.ast.LabeledStatement} object.
	 * @param param a T object.
	 */
	void visitLabeledStatement(LabeledStatement label, T param);

	/**
	 * <p>visitReturnStatement.</p>
	 *
	 * @param r a {@link org.mozilla.javascript.ast.ReturnStatement} object.
	 * @param param a T object.
	 */
	void visitReturnStatement(ReturnStatement r, T param);

	/**
	 * <p>visitSwitchStatement.</p>
	 *
	 * @param s a {@link org.mozilla.javascript.ast.SwitchStatement} object.
	 * @param param a T object.
	 */
	void visitSwitchStatement(SwitchStatement s, T param);

	/**
	 * <p>visitFunctionCall.</p>
	 *
	 * @param fc a {@link org.mozilla.javascript.ast.FunctionCall} object.
	 * @param param a T object.
	 */
	void visitFunctionCall(FunctionCall fc, T param);

	/**
	 * <p>visitPropertyGet.</p>
	 *
	 * @param p a {@link org.mozilla.javascript.ast.PropertyGet} object.
	 * @param param a T object.
	 * @param param a T object.
	 */
	void visitPropertyGet(PropertyGet p, T param);

	/**
	 * <p>visitVariableDeclaration.</p>
	 *
	 * @param v a {@link org.mozilla.javascript.ast.VariableDeclaration} object.
	 * @param param a T object.
	 */
	void visitVariableDeclaration(VariableDeclaration v, T param);

	/**
	 * <p>visitVariableInitializer.</p>
	 *
	 * @param v a {@link org.mozilla.javascript.ast.VariableInitializer} object.
	 * @param param a T object.
	 */
	void visitVariableInitializer(VariableInitializer v, T param);

	/**
	 * <p>visitTryStatement.</p>
	 *
	 * @param t a {@link org.mozilla.javascript.ast.TryStatement} object.
	 * @param param a T object.
	 */
	void visitTryStatement(TryStatement t, T param);

	/**
	 * <p>visitWhileLoop.</p>
	 *
	 * @param w a {@link org.mozilla.javascript.ast.WhileLoop} object.
	 * @param param a T object.
	 */
	void visitWhileLoop(WhileLoop w, T param);

	/**
	 * <p>visitAstRoot.</p>
	 *
	 * @param r a {@link org.mozilla.javascript.ast.AstRoot} object.
	 * @param param a T object.
	 */
	void visitAstRoot(AstRoot r, T param);

	/**
	 * <p>visitConditionalExpression.</p>
	 *
	 * @param c a {@link org.mozilla.javascript.ast.ConditionalExpression} object.
	 * @param param a T object.
	 */
	void visitConditionalExpression(ConditionalExpression c, T param);

	/**
	 * <p>visitNumberLitera.</p>
	 *
	 * @param n a {@link org.mozilla.javascript.ast.NumberLiteral} object.
	 * @param param a T object.
	 */
	void visitNumberLitera(NumberLiteral n, T param);

	/**
	 * <p>visitStatements.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.Statements} object.
	 * @param param a T object.
	 */
	void visitStatements(Statements s, T param);

	/**
	 * <p>visitAssignment.</p>
	 *
	 * @param a a {@link org.mozilla.javascript.ast.Assignment} object.
	 * @param param a T object.
	 */
	void visitAssignment(Assignment a, T param);

	/**
	 * <p>visitInfixExpression.</p>
	 *
	 * @param ie a {@link org.mozilla.javascript.ast.InfixExpression} object.
	 * @param param a T object.
	 */
	void visitInfixExpression(InfixExpression ie, T param);

	/**
	 * <p>visitKeywordLiteral.</p>
	 *
	 * @param k a {@link org.mozilla.javascript.ast.KeywordLiteral} object.
	 * @param param a T object.
	 */
	void visitKeywordLiteral(KeywordLiteral k, T param);

	/**
	 * <p>visitUnaryExpression.</p>
	 *
	 * @param u a {@link org.mozilla.javascript.ast.UnaryExpression} object.
	 * @param param a T object.
	 */
	void visitUnaryExpression(UnaryExpression u, T param);

	/**
	 * <p>visitExpressionStatement.</p>
	 *
	 * @param e a {@link org.mozilla.javascript.ast.ExpressionStatement} object.
	 * @param param a T object.
	 */
	void visitExpressionStatement(ExpressionStatement e, T param);

	/**
	 * <p>visitThrowStatement.</p>
	 *
	 * @param e a {@link org.mozilla.javascript.ast.ThrowStatement} object.
	 * @param param a T object.
	 */
	void visitThrowStatement(ThrowStatement e, T param);

	/**
	 * <p>visitCodeFragment.</p>
	 *
	 * @param c a {@link org.stjs.generator.javascript.rhino.CodeFragment} object.
	 * @param param a T object.
	 */
	void visitCodeFragment(CodeFragment c, T param);

	/**
	 * <p>visitEnum.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.Enum} object.
	 * @param param a T object.
	 */
	void visitEnum(Enum s, T param);

	/**
	 * <p>visitInterfaceDeclaration.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.InterfaceDeclaration} object.
	 * @param param a T object.
	 */
	void visitInterfaceDeclaration(InterfaceDeclaration s, T param);

	/**
	 * <p>visitClassDeclaration.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.ClassDeclaration} object.
	 * @param param a T object.
	 */
	void visitClassDeclaration(ClassDeclaration s, T param);

	/**
	 * <p>visitGenericType.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.GenericType} object.
	 * @param param a T object.
	 */
	void visitGenericType(GenericType s, T param);

	/**
	 * <p>visitVararg.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.Vararg} object.
	 * @param param a T object.
	 */
	void visitVararg(Vararg s, T param);

	/**
	 * <p>visitFieldNode.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.FieldNode} object.
	 * @param param a T object.
	 */
	void visitFieldNode(FieldNode s, T param);

	/**
	 * <p>visitFieldNode.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.ParamNode} object.
	 * @param param a T object.
	 */
	void visitParam(ParamNode s, T param);

	/**
	 * <p>visitFieldNode.</p>
	 *
	 * @param s a {@link org.stjs.generator.javascript.rhino.types.TypeVariableNode} object.
	 * @param param a T object.
	 */
	void visitVariableType(TypeVariableNode s, T param);
}
